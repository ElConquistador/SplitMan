package elcon.games.splitman.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import elcon.games.splitman.util.Util;

public class LanguageManager {

	private static String currentLanguage = "english";
	private static HashMap<String, Language> languages = new HashMap<String, Language>();
	private static boolean loaded = false;

	public static void load() {
		if(!loaded) {
			languages.put("english", new Language("english"));
			//TODO: load languages
		}
		loaded = true;
	}
	
	public static void loadLanguage(File file) {
		String dirName = file.getParentFile().getName();
		String fileName = file.getName();
		String language = fileName.replace(Util.getFileExtension(fileName), "").replace(".", "");
		if(!languages.containsKey(language)) {
			languages.put(language, new Language(language));
		}
		try {
			System.out.println("[LanguageManager] Loading language file: " + dirName + "/" + fileName);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) {
				if(!line.startsWith("#") && line.contains("=")) {
					String[] split = line.split("=");
					if(split.length == 2) {
						setLocatization(language, split[0], split[1]);
					}
				}
			}
			reader.close();
		} catch(Exception e) {
			System.err.println("[LanguageManger] Error while reading language file (" + fileName + "): ");
			e.printStackTrace();
		}
	}
	
	/*public static void load() {
		if(!loaded) {
			languages.put("en_US", new Language("en_US"));
			ElConQore.log.info("[LanguageManager] Loading languages...");
			for(String modName : EQMod.mods.keySet()) {
				if(!modName.equals("ElConQore")) {
					ElConQore.log.info("[LanguageManager] Loading languages for mod: " + modName);
					File sourceFile = EQMod.mods.get(modName).sourceFile;
					File languageDirectory = new File(ElConQore.minecraftDir, "/lang/" + modName.toLowerCase() + "/");
					languageDirectory.delete();
					languageDirectory.mkdirs();
					if(sourceFile.isDirectory()) {
						ElConQore.log.info("[LanguageManager] Copying files from " + new File(sourceFile, "/assets/" + modName.toLowerCase() + "/lang/").getAbsolutePath() + " folder to " + languageDirectory.getAbsolutePath());
						try {
							FileUtils.copyDirectory(new File(sourceFile, "/assets/" + modName.toLowerCase() + "/lang/"), languageDirectory);
						} catch(Exception e) {
							e.printStackTrace();
						}
					} else {
						ElConQore.log.info("[LanguageManager] Copying files from jar (" + sourceFile.getAbsolutePath() + ") to " + languageDirectory.getAbsolutePath());
						try {
							JarFile jar = new JarFile(sourceFile);
							Enumeration<JarEntry> entries = jar.entries();
							while(entries.hasMoreElements()) {
								JarEntry jarEntry = (JarEntry) entries.nextElement();
								if(jarEntry.getName().contains("assets/" + modName.toLowerCase() + "/lang/")) {
									File f = new File(languageDirectory, jarEntry.getName().replace("assets/" + modName.toLowerCase() + "/lang/", ""));
									if(jarEntry.isDirectory()) {
										f.mkdirs();
										continue;
									}
									if(!f.exists()) {
										f.createNewFile();
									}
									InputStream is = jar.getInputStream(jarEntry);
									FileOutputStream fos = new FileOutputStream(f);
									while(is.available() > 0) {
										fos.write(is.read());
									}
									fos.close();
									is.close();
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					if(!downloaded) {
						EQMod mod = EQMod.mods.get(modName);
						for(String localizationURL : mod.localizationURLs) {
							try {
								File file = new File(languageDirectory, modName.toLowerCase() + ".zip");
								FileUtils.copyURLToFile(new URL(localizationURL), file);
								ElConQore.log.info("[LanguageManager] Downloaded localization for " + modName + " from " + localizationURL);
								if(file.exists()) {
									EQUtil.extractZip(file, languageDirectory, true);
									ElConQore.log.info("[LanguageManager] Extracted localization for " + modName);
								}
								file.delete();
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					}
					searchLanguageDirectory(languageDirectory);
				}
			}
			loaded = true;
			downloaded = true;
		}
	}

	private static void searchLanguageDirectory(File dir) {
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() || EQUtil.getFileExtension(file).equals("lang");
			}
		});
		for(int i = 0; i < files.length; i++) {
			if(files[i] != null) {
				if(files[i].isDirectory()) {
					searchLanguageDirectory(files[i]);
				} else {
					loadLanguageFile(files[i]);
				}
			}
		}
	}*/

	public static String getCurrentLanguage() {
		return currentLanguage;
	}

	public static void setCurrentLanguage(String language) {
		currentLanguage = language;
	}

	public static Language getLanguage() {
		return getLanguage(currentLanguage);
	}

	public static Language getLanguage(String name) {
		if(languages.containsKey(name)) {
			return languages.get(name);
		}
		return languages.get("en_US");
	}

	public static void setLanguage(String name, Language language) {
		languages.put(name, language);
	}

	public static String getLocalization(String key) {
		if(!loaded) {
			return key;
		}
		return getLanguage().getLocalization(key);
	}

	public static void setLocatization(String key, String localization) {
		getLanguage().setLocalization(key, localization);
	}

	public static String getLocalization(String language, String key) {
		if(!loaded) {
			return key;
		}
		return getLanguage(language).getLocalization(key);
	}

	public static void setLocatization(String language, String key, String localization) {
		getLanguage(language).setLocalization(key, localization);
	}

	public static void setLoaded(boolean flag) {
		loaded = flag;
	}
}
