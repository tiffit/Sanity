package net.tiffit.sanity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;

@Config(modid = Sanity.MODID, name="sanity/sanity")
public class ConfigHelper {
	
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_ghostzombie_veryhealthy = 0;
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_ghostzombie_healthy = 0;
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_ghostzombie_damaged = 1 / (20 * 60 * 20.0);
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_ghostzombie_verydamaged = 1 / (20 * 60 * 7.0);
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_ghostzombie_insane = 1 / (20 * 60 * 2.0);
	@RangeInt(min = 0)
	public static int spawn_rate_ghostzombie_cooldown = 20*10;
	
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_lightseeker_veryhealthy = 0;
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_lightseeker_healthy = 0;
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_lightseeker_damaged = 1 / (20 * 60 * 45.0);
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_lightseeker_verydamaged = 1 / (20 * 60 * 20.0);
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_lightseeker_insane = 1 / (20 * 60 * 5.0);
	@RangeInt(min = 0)
	public static int spawn_rate_lightseeker_cooldown = 20*15;
	
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_eyes_veryhealthy = 0;
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_eyes_healthy = 0;
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_eyes_damaged = 1 / (20 * 60 * 3.0);
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_eyes_verydamaged = 1 / (20 * 60 * 1.0);
	@RangeDouble(min = 0, max = 1)
	public static double spawn_rate_eyes_insane = 1 / (20 * 60 * 0.25);
	@RangeInt(min = 0)
	public static int spawn_rate_eyes_cooldown = 40;

	public static void loadModifierMap(HashMap<String, List<SanityModifier>> map){
		try {
			saveDefaultModifiers(map);
			File file = new File(Sanity.CONFIG_DIR, "modifiers.xml");
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			doc.getDocumentElement().normalize();
			map.clear();
			NodeList modList = doc.getElementsByTagName("modifier");
			for(int i = 0; i < modList.getLength(); i++){
				Element mod = (Element) modList.item(i);
				List<SanityModifier> list = new ArrayList<>();
				NodeList entryList = mod.getElementsByTagName("entry");
				for(int j = 0; j < entryList.getLength(); j++){
					Element entry = (Element) entryList.item(j);
					try{
						float amount = Float.valueOf(entry.getAttribute("value"));
						list.add(new SanityModifier(entry.getTextContent(), amount));
					}catch (NumberFormatException e) {
						Sanity.logger.error(entry.getAttribute("value") + " is not a valid number type!");
						continue;
					}
				}
				map.put(mod.getAttribute("value"), list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void saveDefaultModifiers(HashMap<String, List<SanityModifier>> map) throws Exception{
		File file = new File(Sanity.CONFIG_DIR, "modifiers.xml");
		if(!file.exists())file.createNewFile();
		else return;
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element root = doc.createElement("modifiers");
		doc.appendChild(root);
		for(Entry<String, List<SanityModifier>> me : map.entrySet()){
			Element type = doc.createElement("modifier");
			type.setAttribute("value", me.getKey());
			for(SanityModifier mod : me.getValue()){
				Element elemMod = doc.createElement("entry");
				elemMod.setTextContent(mod.value);
				elemMod.setAttribute("value", mod.amount + "");
				type.appendChild(elemMod);
			}
			root.appendChild(type);
		}
		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setAttribute("indent-number", 2); 
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		Result output = new StreamResult(file);
		Source input = new DOMSource(doc);
		transformer.transform(input, output);
	}
	
}
