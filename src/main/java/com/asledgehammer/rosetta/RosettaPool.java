package com.asledgehammer.rosetta;

import com.asledgehammer.rosetta.exception.RosettaException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * RosettaPools store a group or collection of Rosetta data in a pooled way.
 *
 * <p>Rosetta has two main dataset schema types:
 *
 * <ul>
 *   <li>Language-specific: Language-generic datasets.
 *   <li>Application-specific: Application-specific datasets of language data structured in a way
 *       that applies to said-application.
 * </ul>
 *
 * <p>NOTE: The implementing code must decide how to syndicate serialized Rosetta pools. One way is
 * to pool for each file if the purpose is to load, read, modify and save the same data in the same
 * container. Another way is to handle all Rosetta data in one pool and serialize into one
 * container.
 */
public class RosettaPool {

  private final Map<String, RosettaLanguage> languages = new HashMap<>();
  private final Map<String, RosettaApplication> applications = new HashMap<>();

  public RosettaPool() {}

  @SuppressWarnings({"unchecked"})
  public void onLoad(@NotNull Map<String, Object> raw) {
    // Grab the key.
    if (!raw.containsKey("version")) {
      throw new RosettaException("Missing \"version\" property at root of Rosetta YAML file.");
    }

    // If multi-version support in the future, convert to switch-table.
    final String version = raw.get("version").toString().trim();
    if (!version.equals("1.2")) {
      throw new RosettaException("Unknown version: " + version);
    }

    if (!raw.containsKey("languages")) {
      // No definitions? Return.
      return;
    }

    final Object oLanguages = raw.get("languages");
    if (!(oLanguages instanceof Map)) {
      throw new RosettaException("The property \"languages\" is not a dictionary.");
    }
    onLoadLanguages((Map<String, Object>) oLanguages);
  }

  @SuppressWarnings({"unchecked"})
  private void onLoadLanguages(@NotNull Map<String, Object> languages) {
    final List<String> keys = new ArrayList<>(languages.keySet());
    keys.sort(Comparator.naturalOrder());

    for (String key : keys) {
      String keyLower = key.toLowerCase().trim();
      RosettaLanguage language;
      if (!hasLanguage(keyLower)) {
        language = Rosetta.createLanguage(keyLower);
        this.languages.put(keyLower, language);
      } else {
        language = this.languages.get(keyLower);
      }
      language.onLoad((Map<String, Object>) languages.get(key));
    }
  }

  @SuppressWarnings({"unchecked"})
  private void onLoadApplications(@NotNull Map<String, Object> applications) {
    final List<String> keys = new ArrayList<>(applications.keySet());
    keys.sort(Comparator.naturalOrder());

    for (String key : keys) {
      String keyLower = key.toLowerCase().trim();
      RosettaApplication application;
      if (!hasLanguage(keyLower)) {
        application = Rosetta.createApplication(keyLower);
        this.applications.put(keyLower, application);
      } else {
        application = this.applications.get(keyLower);
      }
      application.onLoad((Map<String, Object>) applications.get(key));
    }
  }

  @NotNull
  public Map<String, Object> onSave() {
    // TODO: Implement.
    return Map.of();
  }

  /**
   * Adds a RosettaLanguage to the Rosetta pool.
   *
   * @param lang The RosettaLanguage to register.
   * @throws IllegalArgumentException If the RosettaLanguage's ID marches an existing
   *     RosettaLanguage object that's already in the pool.
   */
  public void addLanguage(@NotNull RosettaLanguage lang) {
    String idLower = lang.getID().toLowerCase();
    if (languages.containsKey(idLower)) {
      throw new IllegalArgumentException(
          "A RosettaLanguage already exists with the ID: " + idLower);
    }

    languages.put(idLower, lang);
  }

  /**
   * Removes a RosettaLanguage from the Rosetta pool.
   *
   * @param lang The RosettaLanguage to unregister.
   * @throws IllegalArgumentException If the RosettaLanguage's ID doesn't march an existing
   *     RosettaLanguage object that's in the pool.
   */
  public void removeLanguage(@NotNull RosettaLanguage lang) {

    String idLower = lang.getID().toLowerCase();
    if (!languages.containsKey(idLower)) {
      throw new NullPointerException("No RosettaLanguage is registered with the ID: " + idLower);
    }

    languages.remove(lang.getID());
  }

  /**
   * Removes a RosettaLanguage from the Rosetta pool.
   *
   * @param id The YAML ID of the RosettaLanguage to unregister.
   * @return The unregistered RosettaLanguage.
   * @throws IllegalArgumentException If the RosettaLanguage's ID doesn't march an existing
   *     RosettaLanguage object that's in the pool.
   */
  @NotNull
  public RosettaLanguage removeLanguage(@NotNull String id) {

    String idLower = id.toLowerCase();
    if (!languages.containsKey(idLower)) {
      throw new NullPointerException("No RosettaLanguage is registered with the ID: " + idLower);
    }

    return languages.remove(id.toLowerCase());
  }

  /**
   * @param id The YAML language ID.
   * @return The registered RosettaLanguage object.
   * @throws NullPointerException If no RosettaLanguage object is registered with the ID. Use {@link
   *     RosettaPool#hasLanguage(String)} to see if a RosettaLanguage is registered with the ID
   *     before invoking this method.
   */
  public RosettaLanguage getLanguage(@NotNull String id) {

    String idLower = id.toLowerCase();
    if (!languages.containsKey(idLower)) {
      throw new NullPointerException("No RosettaLanguage is registered with the ID: " + idLower);
    }

    return languages.get(id);
  }

  /**
   * @param lang The RosettaLanguage to test.
   * @return True if the RosettaLanguage is registered.
   */
  public boolean hasLanguage(@NotNull RosettaLanguage lang) {
    return hasLanguage(lang.getID());
  }

  /**
   * @param id The RosettaLanguage ID to test.
   * @return True if a RosettaLanguage with the ID is registered.
   */
  public boolean hasLanguage(@NotNull String id) {
    return languages.containsKey(id.toLowerCase());
  }

  /**
   * Adds a RosettaApplication to the pool.
   *
   * @param app The app to register.
   * @throws IllegalArgumentException If the RosettaApplication's ID marches an existing
   *     RosettaApplication object that's already in the pool.
   */
  public void addApplication(@NotNull RosettaApplication app) {
    String idLower = app.getID().toLowerCase();
    if (applications.containsKey(idLower)) {
      throw new IllegalArgumentException(
          "A RosettaApplication already exists with the ID: " + idLower);
    }

    applications.put(idLower, app);
  }

  /**
   * Removes a RosettaApplication from the Rosetta pool.
   *
   * @param app The RosettaApplication to unregister.
   * @throws IllegalArgumentException If the RosettaApplication's ID doesn't march an existing
   *     RosettaApplication object that's in the pool.
   */
  public void removeApplication(@NotNull RosettaApplication app) {

    String idLower = app.getID().toLowerCase();
    if (!applications.containsKey(idLower)) {
      throw new NullPointerException("No RosettaApplication is registered with the ID: " + idLower);
    }

    applications.remove(app.getID());
  }

  /**
   * Removes a RosettaApplication from the Rosetta pool.
   *
   * @param id The YAML ID of the RosettaApplication to unregister.
   * @return The unregistered RosettaApplication.
   * @throws IllegalArgumentException If the RosettaApplication's ID doesn't march an existing
   *     RosettaApplication object that's in the pool.
   */
  @NotNull
  public RosettaApplication removeApplication(@NotNull String id) {

    String idLower = id.toLowerCase();
    if (!applications.containsKey(idLower)) {
      throw new NullPointerException("No RosettaApplication is registered with the ID: " + idLower);
    }

    return applications.remove(id.toLowerCase());
  }

  /**
   * @param id The YAML language ID.
   * @return The registered RosettaApplication object.
   * @throws NullPointerException If no RosettaApplication object is registered with the ID. Use
   *     {@link RosettaPool#hasLanguage(String)} to see if a RosettaApplication is registered with
   *     the ID before invoking this method.
   */
  public RosettaApplication getApplication(@NotNull String id) {

    String idLower = id.toLowerCase();
    if (!applications.containsKey(idLower)) {
      throw new NullPointerException("No RosettaApplication is registered with the ID: " + idLower);
    }

    return applications.get(id);
  }

  /**
   * @param pool The RosettaApplication to test.
   * @return True if the RosettaApplication is registered in the pool.
   */
  public boolean hasApplication(@NotNull RosettaApplication pool) {
    return hasApplication(pool.getID());
  }

  /**
   * @param id The RosettaApplication ID to test.
   * @return True if a RosettaApplication with the ID is registered in the pool.
   */
  public boolean hasApplication(@NotNull String id) {
    return applications.containsKey(id.toLowerCase());
  }
}
