package de.jonas;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import aventurian.Race;
import skills.InstantiableSkill;
import skills.attributes.primary.PrimaryAttribute;
import skills.attributes.secondary.SecondaryAttribute;
import skills.languages.Language;
import skills.properties.BadProperty;
import skills.properties.Property;

public class Database {

	private static final RaceConfiguration DEFAULT_RACECONFIGURATION = new RaceConfiguration(0, 0, new ArrayList<>());
	private List<Property> properties;
	private List<Language> languages;
	private Map<Race, RaceConfiguration> races;
	private ArrayList<PrimaryAttribute> primaryAttributes;
	private List<SecondaryAttribute> secondaryAttributes;

	public Database() {
		reset();
	}

	public List<Language> getLanguages() {
		return languages.stream().sorted().collect(toList());
	}

	public List<PrimaryAttribute> getPrimaryAttributes() {
		return primaryAttributes.stream().sorted().collect(toList());
	}

	public List<SecondaryAttribute> getSecondaryAttributes() {
		return secondaryAttributes.stream().sorted().collect(toList());
	}

	public SecondaryAttribute getSecondaryAttribute(String name) {
		return secondaryAttributes.stream().filter(p -> name.equals(p.getName())).findFirst()
				.orElseThrow(() -> new IllegalStateException("could not find " + name + " in database!"));
	}

	public List<Property> getAdvantages() {
		return properties.stream().filter(Property::isAdvantage).collect(toList());
	}

	public List<Property> getDisadvantages() {
		return properties.stream().filter(Property::isDisadvantage).collect(toList());
	}

	public List<Property> getSkillsFor(Race race) {
		final List<String> skillNames = races.getOrDefault(race, DEFAULT_RACECONFIGURATION).getSkillNames();
		return properties.stream().filter(p -> skillNames.contains(p.getName())).collect(toList());
	}

	public void reset() {

		final Reflections reflections = new Reflections(
				new ConfigurationBuilder().setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
						.setUrls(ClasspathHelper.forPackage("skills"))
						.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("skills"))));

		initRaces();
		initProperties(reflections);
		initLanguages(reflections);
		initPrimaryAttributes(reflections);
		initSecondaryAttributes(reflections);
	}

	private void initLanguages(Reflections reflections) {
		languages = new ArrayList<>();
		try {
			final Set<Class<?>> languageClasses = findClasses(reflections, InstantiableSkill.SkillType.LANGUAGE);
			for (final Class<?> clazz : languageClasses) {
				languages.add((Language) clazz.newInstance());
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private Set<Class<?>> findClasses(Reflections reflections, InstantiableSkill.SkillType t) {
		return reflections.getTypesAnnotatedWith(InstantiableSkill.class).stream()
				.filter(c -> c.getAnnotation(InstantiableSkill.class).value().equals(t)).collect(Collectors.toSet());
	}

	private void initPrimaryAttributes(Reflections reflections) {
		primaryAttributes = new ArrayList<>();
		try {
			final Set<Class<?>> primaryAttributeClasses = findClasses(reflections,
					InstantiableSkill.SkillType.PRIMARY_ATTRIBUTE);
			for (final Class<?> clazz : primaryAttributeClasses) {
				primaryAttributes.add((PrimaryAttribute) clazz.newInstance());
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void initSecondaryAttributes(Reflections reflections) {
		secondaryAttributes = new ArrayList<>();
		try {
			final Set<Class<?>> secondaryAttributeClasses = findClasses(reflections,
					InstantiableSkill.SkillType.SECONDARY_ATTRIBUTE);
			for (final Class<?> clazz : secondaryAttributeClasses) {
				secondaryAttributes.add((SecondaryAttribute) clazz.newInstance());
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void initProperties(Reflections reflections) {
		properties = new ArrayList<>();
		try {
			final Set<Class<?>> propertyClasses = findClasses(reflections, InstantiableSkill.SkillType.PROPERTY);
			for (final Class<?> clazz : propertyClasses) {
				properties.add((Property) clazz.newInstance());
			}
			final Set<Class<?>> badPropertyClasses = findClasses(reflections, InstantiableSkill.SkillType.BADPROPERTY);
			for (final Class<?> clazz : badPropertyClasses) {
				properties.add((BadProperty) clazz.newInstance());
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// properties.add(new Property("Adliges Erbe", "Nur für adlige Charaktere.\r\n"
		// + "Der Charakter ist der erste in der Erbfolge und es ist zu erwarten, dass
		// er das Erbe seines Vaters oder seiner Mutter antritt, sollte diese/r
		// dahinscheiden.",
		// 250, EMPTY, EMPTY,
		// (Aventurian a) -> a.hasSkill("Adlig I") || a.hasSkill("Adlig II") ||
		// a.hasSkill("Adlig III")));
		// properties.add(new Property("Amtsadel",
		// "Der Charakter ist nicht von adliger Geburt, hat aber durch seinen Rang oder
		// seinen Beruf einen ähnlichen, wenn auch nicht so ausgeprägten, Titel und
		// damit vergleichbare Vorteile. ",
		// 150, EMPTY, EMPTY, NOREQUIREMENT));
		// properties.add(new Property("Altersresistenz", "Normalerweise nur für Elfen
		// und Zwerge.\r\n"
		// + "Der Charakter altert merklich langsamer. Sein Aussehen und seine Werte
		// verändern sich dementsprechend anders.",
		// 50, EMPTY, EMPTY, NOREQUIREMENT));
		// properties.add(new Property("Ausdauernd",
		// "Die Erschöpfungsschwelle des Charakters erhöht sich um 1. \n Kann nicht mit
		// 'Kurzatmig' gewählt werden!",
		// 150, (Aventurian a) ->
		// a.increaseSecondaryAttribute(SECONDARY_ATTRIBUTE.EXHAUSTIONTHRESHHOLD, 1),
		// (Aventurian a) ->
		// a.decreaseSecondaryAttribute(SECONDARY_ATTRIBUTE.EXHAUSTIONTHRESHHOLD, 1),
		// (Aventurian a) -> !a.hasSkill("Kurzatmig")));
		// funktioniert bisher noch nicht. benötigt konstante kosten.
		// properties.add(new Property("Ausrüstungsvorteil", "Pro Stufe erhält der
		// Charakter 15D mehr Startgeld. Kostet 50 AP pro Stufe.", cost, effectOnGain,
		// effectOnLose, requirement, minLevel, maxLevel))
		// funktioniert auch nicht ganz. benötigt konstante kosten.
		// properties.add(new Property("Besonderer Besitz",
		// "Nach Absprache mit dem Meister erhält der Charakter einen mittelmächtigen
		// Gegenstand zum Spielstart.",
		// 350, EMPTY, EMPTY, NOREQUIREMENT));
		// properties.add(new Property("Daemmerungssicht",
		// "Charaktere mit Dunkelsicht halbieren die Abzuege durch fehlendes Licht,
		// außer bei absoluter Dunkelheit. \n Kann nicht mit 'Nachtblind' gewählt
		// werden!",
		// 250, EMPTY, EMPTY, (Aventurian a) -> !a.hasSkill("Nachtblind")));
		// properties.add(new Property("Dichschaedel",
		// "Auch am Kopf werden nur 100% TP(B) verursacht. \nDas Manoever Kopfstoß ist
		// um 2 Punkte erleichtert und verursacht 1 TP(B) mehr.",
		// 100, EMPTY, EMPTY, NOREQUIREMENT));
		// properties.add(
		// new Property("Eisern", "Erhoet die Wundschwelle um 1. \nKann nicht mit
		// Glasknochen gewaehlt werden.",
		// 200, (Aventurian a) ->
		// a.increaseSecondaryAttribute(SECONDARY_ATTRIBUTE.WOUNDTHRESHHOLD, 1),
		// (Aventurian a) ->
		// a.decreaseSecondaryAttribute(SECONDARY_ATTRIBUTE.WOUNDTHRESHHOLD, 1),
		// (Aventurian a) -> !a.hasSkill("Glasknochen")));
		// properties.add(new Property("Entfernungssinn",
		// "IN-Proben zum Einschätzen der Entfernung sind um bis zu 5 Punkte
		// erleichtert. Beim Fernkampf sind die Proben auf Ziele, die mindestens 50
		// Schritt entfernt sind, nicht erschwert.",
		// 150, EMPTY, EMPTY, NOREQUIREMENT));
		// properties.add(new Property("Feenfreund", "Feen helfen haeufiger und ohne
		// eine Gegenleistung zu erwarten.", 250,
		// EMPTY, EMPTY, NOREQUIREMENT));
		// // Maybe add effectOnGain for dodging.
		// properties.add(new Property("Flink",
		// "Ausweichen um 1 erhöht. Außerdem sind Athletik-Proben für Geschwindigkeit
		// (Verfolgung, Wettrennen etc.) um 3 Punkte erleichtert. Diese Punkte können
		// auch als TaP* überbehalten werden.",
		// 300, EMPTY, EMPTY, (Aventurian a) -> !a.hasSkill("Behaebig")));
		// // requirement needs to be added
		// properties.add(new Property("Glueck",
		// "Kann pro aventurischem Tag 1W3-1 Würfelwürfe wiederholen (oder vom Meister
		// wiederholen lassen) und das bessere Ergebnis wählen.",
		// 600, EMPTY, EMPTY, NOREQUIREMENT));
		// properties.add(new Property("Glueck im Spiel",
		// "Erleichtert Proben auf Brett-/Kartenspiel um bis zu 7 Punkte, je nachdem wie
		// viel Glück im Spiel vorhanden ist.",
		// 100, EMPTY, EMPTY, NOREQUIREMENT));
		// properties.add(new Property("Gutes Aussehen",
		// "Erleichtert passende Proben auf Interaktionstalente um 2/4/6 Punkte. \nKann
		// nicht mit 'Unansehnlich' oder 'Widerwärtiges Aussehen' gewählt werden!",
		// 250, EMPTY, EMPTY,
		// (Aventurian a) -> !a.hasSkill("Unansehnlich") && !a.hasSkill("Widerwärtiges
		// Aussehen"), 1, 3));
		//
		// properties.add(new Property("Nachtsicht",
		// "Der Charakter ignoriert 6 Stufen fehlenden Lichts, außer bei absoluter
		// Dunkelheit.\n Kann nicht mit 'Nachtblind' gewählt werden!",
		// 750, EMPTY, EMPTY, (Aventurian a) -> !a.hasSkill("Nachtblind")));

		// properties.add(new Property("Richtungssinn", "gutes Gespür fuer Richtungen",
		// 150, EMPTY, EMPTY, NOREQUIREMENT));
		// properties.add(new Property("Gestank", "bestialischer Gestank", -150, EMPTY,
		// EMPTY, NOREQUIREMENT));

		// properties.add(new BadProperty("Jaehzorn", "wirklich wuetend", -75,
		// NOREQUIREMENT));
	}

	private void initRaces() {
		races = new HashMap<>();
		races.put(Race.MIDDLEGUY, new RaceConfiguration(10, -4, new ArrayList<>()));
		races.put(Race.THORWALAN, new RaceConfiguration(11, -5, Arrays.asList("Jähzorn")));
	}

	public int getHitPointsModFor(Race race) {
		return races.getOrDefault(race, DEFAULT_RACECONFIGURATION).getHitPointsMod();
	}

	public int getMagicResistanceModFor(Race race) {
		return races.getOrDefault(race, DEFAULT_RACECONFIGURATION).getMagicResistanceMod();
	}

}
