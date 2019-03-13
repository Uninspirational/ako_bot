package com.AkoBot.Bandori;

import com.google.gson.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class BandoriSongs {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(BandoriSongs.class);

    private Hashtable<String, BandoriCollection> hashtable = new Hashtable<>(500);
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<BandoriSong> collection;

    public void BandoriRefresh() {
        try {
//            first URL link to api
//            search for all images
//            format json
//            display page id
//            aisort by name in ascending order
//            display url and mediatype

            String urlString = "https://bandori.fandom.com/api.php?action=query&format=json&list=allimages&indexpageids=1&rawcontinue=1&aisort=name&aidir=descending&aiprop=url%7Csize%7Cmediatype&aiminsize=1000000&ailimit=max";
            collection = new ArrayList<>();
            JsonArray allimages;
            JsonObject next, rootobj, queries;
            int i;
            boolean flag = true;
            String name, url, aifrom;
            do {
                //JsonObject to entire page of results
                rootobj = getJsonObjectFromUrl(urlString);

                //isolate the query object
                queries = rootobj.getAsJsonObject("query");

                //isolate the allimages object
                allimages = queries.getAsJsonArray("allimages");

                //identify the next page of results and check to see if at end
                try {
                    next = rootobj.getAsJsonObject("query-continue").getAsJsonObject("allimages");
                    //only add audio files to arraylist
                    for (i = 0; i < allimages.size(); i++) {
                        if (allimages.get(i).getAsJsonObject().getAsJsonPrimitive("mediatype").getAsString().equals("AUDIO")) {
                            url = allimages.get(i).getAsJsonObject().getAsJsonPrimitive("url").getAsString();
                            name = allimages.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString();
                            System.out.println("name: " + name);
                            collection.add(getBandoriSong(name, url));
                        }
                    }

                    //stop at last song


                    //find URL to next page of results
                    aifrom = next.getAsJsonPrimitive("aifrom").getAsString();

                    aifrom = unicodeFixer(aifrom);
                    aifrom = aifrom.replace("!", "%EF%BC%81");
                    urlString = "https://bandori.fandom.com/api.php?action=query&format=json&list=allimages&indexpageids=1&rawcontinue=1&aisort=name&aidir=descending&aiprop=url%7Csize%7Cmediatype&aiminsize=1000000&ailimit=max&aifrom=" + aifrom;
                }
                catch (NullPointerException g) {
                    flag = false;
                }
            } while (flag);
            //sort songs by alphabetical order and return as array
            BandoriSong[] songs = insertionSort();

            //put songs in hashmap of collection objects
            hashCollection(songs);
        }
        catch (MalformedURLException e) {
            logger.debug("Malformed URL Exception: {}", e);
        }
        catch (IOException f) {
            logger.debug("Generic Exception: {}", f);
        }
    }

    /**
     * get id of page based on song name
     * @param name name of song
     */
    private int getPageId(String name) throws IOException{
        //replace special characters
        name = unicodeFixer(name);

        //find page from api
        String url = "https://bandori.fandom.com/api.php?action=query&prop=revisions&titles=" + name + "&format=json";
        JsonObject jsonObject = getJsonObjectFromUrl(url);
        JsonObject query = jsonObject.getAsJsonObject("query");
        JsonObject pages = query.getAsJsonObject("pages");

        //isolate the id from jsonobject and return as integer
        String idString = pages.getAsJsonObject().toString();
        idString = idString.substring(2);
        idString = idString.substring(0, idString.indexOf("\""));
        return Integer.parseInt(idString);
    }

    /**
     * retrieve wiki url from bandori fandom
     * @param id id to article
     * @return url to wiki
     */
    private String getWikiUrl(int id) throws IOException{
        try {
            //get jsonobject find the url
            JsonObject jsonObject = getJsonObjectFromUrl(articleUrl(id));
            String wikiUrl = jsonObject.getAsJsonPrimitive("basepath").getAsString();
            JsonObject temp = jsonObject.getAsJsonObject("items");
            temp = temp.getAsJsonObject("" + id);
            wikiUrl = wikiUrl.concat(temp.getAsJsonPrimitive("url").getAsString());
            return wikiUrl;
        }
        catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * retrieve title from bandori fandom
     * @param id id to article
     * @return title of article
     */
    private String getTitle(int id) throws IOException{
        //get jsonobject from api and return title
        JsonObject jsonObject = getJsonObjectFromUrl(articleUrl(id));
        JsonObject temp = jsonObject.getAsJsonObject("items");
        temp = temp.getAsJsonObject("" + id);
        return temp.getAsJsonPrimitive("title").getAsString();
    }

    /**
     * retrieve description from bandori fandom
     * @param id id to article
     * @return description of song
     */
    private String getDescription(int id) throws IOException{
        //make sure song has valid id
        if (id == -1)
            return "";
        String description = "";
        String url = "https://bandori.fandom.com/api/v1/Articles/AsSimpleJson?id=" + id;
        JsonObject jsonObject = getJsonObjectFromUrl(url);
        JsonArray jsonArray = jsonObject.getAsJsonArray("sections");
        JsonObject temp = jsonArray.get(0).getAsJsonObject();
        jsonArray = temp.getAsJsonArray("content");
        JsonPrimitive jsonPrimitive;
        String line;
        for (int i = 0; i < jsonArray.size(); i++) {
            temp = jsonArray.get(i).getAsJsonObject();
            jsonPrimitive = temp.getAsJsonPrimitive("text");
            if (jsonPrimitive != null) {
                line = jsonPrimitive.getAsString();
                description = description.concat(line).concat(" ");
            }
            else {
                //if the page has been moved to a different page
                return redirectSolver(url);
            }
        }
        return description;
    }

    /**
     * if wiki page has been moved and must be redirected
     * @param url url to old wiki page
     * @return url to new wiki page
     */
    private String redirectSolver(String url) throws IOException{
        String description, line;
        JsonObject jsonObject = getJsonObjectFromUrl(url);
        JsonArray jsonArray = jsonObject.getAsJsonArray("sections");
        jsonObject = jsonArray.get(0).getAsJsonObject();
        jsonArray = jsonObject.getAsJsonArray("content");
        jsonArray = jsonArray.get(0).getAsJsonObject().getAsJsonArray("elements");
        jsonObject = jsonArray.get(0).getAsJsonObject();
        line = jsonObject.getAsJsonPrimitive("text").getAsString();
        line = line.substring(line.indexOf(" ") + 1);
        line = line.replace(" ", "_");
        description = getDescription(getPageId(line));
        return description;
    }

    /**
     * retrieve thumbnail url from bandori fandom
     * @param id id to article
     * @return url to thumbnail of song
     */

    private String getThumbnail(int id) throws IOException{
        try {
            String url = articleUrl(id);
            JsonObject jsonObject = getJsonObjectFromUrl(url);
            JsonObject temp = jsonObject.getAsJsonObject("items");
            temp = temp.getAsJsonObject("" + id);
            return temp.getAsJsonPrimitive("thumbnail").getAsString();
        }
        catch (ClassCastException e) {
            return "";
        }
        catch (NullPointerException f) {
            return "";
        }
    }

    /**
     * scan name of song to identify song title
     * @param name name of song
     * @return type of song as enum
     */
    private SongType getSongType(String name) {
        name = name.toLowerCase();
        SongType result;
        if (name.contains("game ver")) {
            result = SongType.GAME_VERSION;
        }
        else if (name.contains("instrumental")) {
            result = SongType.INSTRUMENTAL;
        }
        else if (name.contains("remaster")) {
            result = SongType.REMASTER;
        }
        else if (name.contains("remaster") && name.contains("instrumental")) {
            result = SongType.REMASTER_INSTRUMENTAL;
        }
        else if (name.contains("short ver")) {
            result = SongType.SHORT;
        }
        else if (name.contains("the third 1st live")) {
            if (name.contains("encore")) {
                result = SongType.ENCORE_LIVE;
            }
            else {
                result = SongType.LIVE;
            }
        }
        else if (name.contains("acoustic")) {
            if (name.contains("instrumental")) {
                result = SongType.ACOUSTIC_INSTRUMENTAL;
            }
            else if  (name.contains("popipa")) {
                result = SongType.POPIPA_ACOUSTIC;
            }
            else {
                result = SongType.ACOUSTIC;
            }
        }
        else if (name.contains("solo ver")) {
            result = SongType.SOLO;
        }
        else if (name.contains("tv size")) {
            result = SongType.TV_SIZE;
        }
        else {
            result = SongType.FULL_VERSION;
        }
        return result;

    }

    /**
     * manually correct inconsistent naming in API
     * @param name name of song to check
     * @return corrected name
     */

    private String nameCorrecter(String name) {
        if (name.equals("File:Goka Gokai Phantom Thief.ogg")) {
            name = "File:Goka! Gokai!? Phantom Thief!.ogg";
        }
        else if (name.contains("A Cruel Angel's Thesis")) {
            name = "File:A Cruel Angel's Thesis (Game Version).ogg";
        }
        else if (name.contains("Goka Gokai Phantom Thief -Instrumental-"))
        {
            name = "File:Goka! Gokai!? Phantom Thief! -Instrumental-.ogg";
        }
        else if (name.contains("Goka! Gokai! Phantom Thief! (Game Version)")) {
            name = "File:Goka! Gokai!? Phantom Thief! (Game Version).ogg";
        }
        else if (name.contains("Hey-day狂騒曲 -Instrumental-")) {
            name = "File:Hey-day Capriccio -Instrumental-.ogg";
        }
        else if (name.contains("Kirakira Datoka Yume Datoka ~Sing Girls~ (Game Version)")){
            name = "File:Kirakira datoka yume datoka ~Sing Girls~ (Game Version).ogg";
        }
        else if (name.contains("Saa Ikou (Game version)")) {
            name = "File:Saa Ikou! (Game Version).ogg";
        }
        else if (name.contains("Tokimeki Experience (Game Version)")) {
            name = "File:Tokimeki Experience! (Game Version).ogg";
        }
        else if (name.contains("どきどき")) {
            name = name.replace("どきどき", "Dokidoki ");
        }
        else if (name.contains("ティアドロップス ")) {
            name = name.replace("ティアドロップス ", "Teardrops ");
        }
        else if (name.contains("軌跡")) {
            name = name.replace("軌跡", "Kiseki");
        }
        else if (name.contains("陽だまりロードナイト")) {
            name = name.replace("陽だまりロードナイト", "Sunkissed Rhodonite");
        }
        else if (name.contains("Janakya")) {
            name = name.replace("Janakya", "Ja Nakya");
        }
        else if (name.contains("～Sing Girls～")) {
            name = name.replace("～Sing Girls～", "~Sing Girls~");
        }
        else if (name.contains("Wasteland")) {
            name = name.replace("Wasteland", "wasteland");
        }
        else if (name.contains("Re birth day")) {
            name = name.replace("Re birth day", "Re:birth day");
        }
        else if (name.contains("Rebirth day")) {
            name = name.replace("Rebirth day", "Re:birth day");
        }
        else if (name.contains("！")) {
            name = name.replaceAll("！", "!");
        }
        else if (name.contains("Sunglass")) {
            name = name.replace("Sunglass", "Sandglass");
        }
        else if (name.contains("Tenka Toitsu A to Z")) {
            name = name.replaceAll("Tenka Toitsu A to Z", "Tenka Toitsu A to Z☆");
        }
        else if (name.contains("Wonderland girl")) {
            name = name.replace("Wonderland girl", "Wonderland Girl");
        }
        else if (name.contains("YAPPY SCHOOL CARNIVAL")) {
            name = name.replace("YAPPY SCHOOL CARNIVAL", "YAPPY! SCHOOL CARNIVAL☆彡");
        }
        else if (name.contains("secret base kimi ga kureta mono")) {
            name = name.replace("secret base kimi ga kureta mono", "Secret base ~kimi ga kureta mono~");
        }
        else if (name.contains("ring-dong-dance")) {
            name = name.replace("ring-dong-dance", "Yura-Yura_Ring-Dong-Dance");
        }
        else if (name.contains("Shimobenari")) {
            name = name.replace("Shimobenari", "Shimobe Nari");
        }
        else if (name.contains("Pasu Pare Revolution")) {
            name = name.replace("Pasu Pare Revolution", "Pasupa_Revolutions");
        }
        else if (name.contains("STAR BEAT ")) {
            name = name.replace("STAR BEAT ", "STAR BEAT! ");
        }
        return name;
    }
    /**
     * create bandorisong object
     * @param name name of song
     * @param url url to song audio file
     * @return BandoriSong
     */
    private BandoriSong getBandoriSong(String name, String url) throws IOException {
        String title;
        name = nameSplitter(nameCorrecter(name));
        int id = getPageId(name);
        SongType songType = getSongType(name);
        //if song is not the game version, use the title as is
        if (id == -1) {
            title = name;
        }
        //if song is the game version, get the proper title
        else {
            title = getTitle(id);
        }
        return new BandoriSong(title, url, null, id, songType);
    }

    /**
     * return url to article
     * @param id to article
     * @return article url
     */
    private String articleUrl(int id) {
        return "https://bandori.fandom.com/api/v1/Articles/Details?ids=" + id + "&abstract=100&width=200&height=200";
    }

    /**
     * insert sort all the bandori songs
     * @return returns songs as an array
     */
    private BandoriSong[] insertionSort() {
        int i, j, length = collection.size();
        if (length == 0)
            logger.debug("collection is empty could not sort bandorisongs");
        BandoriSong[] songs = new BandoriSong[length];
        BandoriSong pos;

        for (i = 0; i < length; i++) {
            songs[i] = collection.get(i);
        }

        for (i = 1; i < length; i++) {
            pos = songs[i];
            j = i - 1;

            while (j >= 0 && songs[j].getName().compareTo(pos.getName()) > 0) {
                songs[j + 1] = songs[j];
                j--;
            }

            songs[j + 1] = pos;
        }
        return songs;
    }

    /**
     * search through description to find band names
     * @param description song description from getDescription()
     * @return array of band names
     */
    private String[] bandNames(String description) {
        int i = 0;
        String[] names = new String[7];
        description = description.toLowerCase();
        if (description.contains("roselia")) {
            names[i++] = "Roselia";
        }
        if (description.contains("poppin")) {
            names[i++] = "Poppin'Party";
        }
        if (description.contains("afterglow")) {
            names[i++] = "afterglow";
        }
        if (description.contains("pastel")) {
            names[i++] = "Pastel☆Palettes";
        }
        if (description.contains("hello, happy world")) {
            names[i++] = "Hello, Happy World";
        }
        if (description.contains("glitter")) {
            names[i++] = "Glitter☆Green";
        }
        if (description.contains("raise a suilen")) {
            names[i++] = "RAISE A SUILEN";
        }
        if (description.contains("argonavis")) {
            names[i] = "Argonavis";
        }

        return names;
    }

    /**
     * create hashtable of bandori songs
     * @param array sorted array of bandori songs
     */
    private void hashCollection(BandoriSong[] array) {
        int length = array.length;
        int i, id;
        String name, wikiUrl, thumbnail, key, hashName, description;
        String[] bands;
        BandoriSong song;
        try {
            //first run through to create collections for all game version songs
            for (i = 0; i < length; i++) {
                if (array[i].getSongType().equals(SongType.GAME_VERSION)) {
                    song = array[i];
                    //strip names of uniqueness to turn into hash code
                    hashName = removeType(song.getName());
                    hashName = removeSpecial(hashName);
                    key = hashName;
                    //get id, name, wiki url, thumbnail url, and band names
                    id = getPageId(removeType(song.getName()));
                    name = getArticleName(id);
                    wikiUrl = getWikiUrl(id);
                    thumbnail = getThumbnail(id);
                    description = getDescription(id);
                    bands = bandNames(description);
                    //create collection, add song, and add it to hashtable
                    BandoriCollection collection = new BandoriCollection(name, wikiUrl, description, bands, thumbnail, key);
                    song.setBand(collection.getBand());
                    song.setWiki(wikiUrl);
                    collection.addSong(song);
                    this.hashtable.put(key, collection);
                    this.keys.add(key);
                }
            }
            //second pass to add non game version songs
            for (i = 0; i < length; i++) {
                //only try to add songs that are not the game version
                if (!array[i].getSongType().equals(SongType.GAME_VERSION)) {
                    try {
                        //strip names of uniqueness to turn into hash code
                        hashName = removeType(array[i].getName());
                        hashName = removeSpecial(hashName);
                        key = hashName;
                        //add song to hash table
                        song = array[i];
                        song.setBand(hashtable.get(key).getBand());
                        song.setWiki(hashtable.get(key).getWikiUrl());
                        hashtable.get(key).addSong(song);
                    }
                    //if the song does not have a game version

                    catch (NullPointerException h) {
                        //collect all information
                        //name, hashkey, id, wikiUrl, thumbnail, key, description, band
                        song = array[i];
                        name = array[i].getName();
                        hashName = removeType(name);
                        id = getPageId(hashName);
                        wikiUrl = getWikiUrl(id);
                        thumbnail = getThumbnail(id);
                        key = removeSpecial(hashName);
                        description = getDescription(id);
                        bands = bandNames(description);
                        //create collection
                        BandoriCollection collection = new BandoriCollection(name, wikiUrl, description, bands, thumbnail, key);
                        song.setBand(collection.getBand());
                        song.setWiki(wikiUrl);
                        collection.addSong(song);
                        hashtable.put(key, collection);
                        this.keys.add(key);
                    }

                }
            }
        }
        catch (IOException e) {
            logger.debug("" + e);
        }
    }

    /**
     * remove non alphabet characters and replace spaces with underscores
     * "×" is replaced with "x"
     * @param string string to edit
     * @return alphanumeric string with no spaces
     */
    public String removeSpecial(String string) {
        string = string.replace("-", "");
        string = string.replace(" ", "");
        string = string.replace("_", "");
        string = string.replace("－", "");
        string = string.replace("~", "");
        string = string.replace(",", "");
        string = string.replace("!", "");
        string = string.replace("?", "");
        string = string.replace("☆", "");
        string = string.replace("・", "");
        string = string.replace(":", "");
        string = string.replace("'", "");
        string = string.replace("♪", "");
        string = string.replace("∞", "");
        string = string.replace("◎", "");
        string = string.replace(".", "");
        string = string.replace("×", "x");
        string = string.replace("(", "");
        string = string.replace(")", "");
        return string;
    }

    /**
     * remove song type tag at end of song name
     * @param string original song title
     * @return only the song name
     */
    private String removeType(String string) {
        if (string.contains("instrumental")) {
            string = string.replace("instrumental", "");
        }
        if (string.contains("Instrumental")) {
            string = string.replace("Instrumental", "");
        }
        if (string.contains("Game Version")) {
            string = string.replace("Game Version", "");
        }
        if (string.contains("Game version")) {
            string = string.replace("Game version", "");
        }
        if (string.contains("Game VersioN")) {
            string = string.replace("Game VersioN", "");
        }
        if (string.contains("Game Ver.")) {
            string = string.replace("Game Ver.", "");
        }
        if (string.contains("THE THIRD 1st Live")) {
            string = string.replace("THE THIRD 1st Live", "");
        }
        if (string.contains("TV Size")) {
            string = string.replace("TV Size", "");
        }
        if (string.contains("Encore Version")) {
            string = string.replace("Encore Version", "");
        }
        if (string.contains("Popipa Acoustic Ver.")) {
            string = string.replace("Popipa Acoustic Ver.", "");
        }
        if (string.contains("acoustic ver.")) {
            string = string.replace("acoustic ver.", "");
        }
        if (string.contains("Acoustic Ver.")) {
            string = string.replace("Acoustic Ver.", "");
        }
        if (string.contains("Encore Version")) {
            string = string.replace("Encore Version", "");
        }
        if (string.contains("Remaster ver.")) {
            string = string.replace("Remaster ver.", "");
        }
        if (string.contains("Saaya Solo Version")) {
            string = string.replace("Saaya Solo Version", "");
        }
        if (string.contains("instrumental")) {
            string = string.replace("instrumental", "");
        }
        if (string.contains("short ver.")) {
            string = string.replace("short ver.", "");
        }
        if (string.contains("Short Ver.")) {
            string = string.replace("Short Ver.", "");
        }
        string = string.replace(" ()", "");
        string = string.replace("()", "");
        string = string.replace(" ( )", "");
        string = string.replace(" --", "");
        string = string.replace(" ～～", "");
        if (string.contains(" ~~")) {
            string = string.replace(" ~~", "");
        }
        if (string.contains(" ~ ")) {
            string = string.replace(" ~ ", " ~");
        }
        if (string.charAt(string.length() - 1) == ' ') {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    /**
     * look up article name from song id
     * @param id api id of article
     * @return string article name
     */
    private String getArticleName(int id) {
        try {
            JsonObject jsonObject = getJsonObjectFromUrl(articleUrl(id));
            jsonObject = jsonObject.getAsJsonObject("items");
            JsonPrimitive jsonPrimitive = jsonObject.getAsJsonObject(id + "").getAsJsonPrimitive("title");
            return jsonPrimitive.getAsString();
        } catch (IOException e) {
            logger.debug("couldn't get article name", e);
            return "";
        }

    }

    /**
     *
     * @return arraylist of keys as strings
     */
    public ArrayList<String> getKeys() {
        return this.keys;
    }

    /**
     *
     * @return hashtable of all bandori collections
     */
    public Hashtable<String, BandoriCollection> getHashTable() {
        return hashtable;
    }

    /**
     *
     * @return arraylist of bandori songs
     */
    public ArrayList<BandoriSong> getCollection() {
        return collection;
    }

    /**
     *
     * @param urlString url for API call
     * @return jsonobject from url
     */
    private JsonObject getJsonObjectFromUrl(String urlString) throws IOException{
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        InputStreamReader inputStreamReader = new InputStreamReader((InputStream) urlConnection.getContent());
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(inputStreamReader);
        return jsonElement.getAsJsonObject();
    }

    /**
     * Remove .ogg and number at beginning of song
     * @param name title of song
     * @return corrected name
     */
    private String nameSplitter(String name) {
        name = name.substring(5);
        name = name.substring(0, name.length() - 4);
        if (name.startsWith("0")) {
            name = name.substring(2);
        }
        if (name.startsWith(".")) {
            name = name.substring(1);
        }
        if (name.startsWith(" ")) {
            name = name.substring(1);
        }
        if (name.startsWith("10 Romeo")) {
            name = name.replace("10 Romeo", "Romeo");
        }
        return name;
    }

    /**
     * replace special characters with hexadecimal(?) so url links work
     * '!' is left unchanged
     * @param string string to edit
     * @return string most special characters removed
     */
    private String unicodeFixer(String string) {
        for (int i = 0; i < string.length(); i++) {

//            some song names have "!" so cannot be replaced here
//            string = string.replace("!", "%EF%BC%81");
            string = string.replace(" ", "_");

            string = string.replace("?", "%3F");
            string = string.replace("♪", "%E2%99%AA");
            string = string.replace("☆", "%E2%98%86");
            string = string.replace("彡", "%E5%BD%A1");
            string = string.replace("★", "%E2%98%85");
            string = string.replace("&", "%26");
            string = string.replace("～", "%EF%BD%9E");
            string = string.replace("－", "%EF%BC%8D");
            string = string.replace("~", "%7E");
            string = string.replace("'", "%27");

        }
        if (string.contains("lazy") && !string.contains("%22")) {
            string = string.replace("lazy", "%22lazy%22");
        }
        return string;
    }
}