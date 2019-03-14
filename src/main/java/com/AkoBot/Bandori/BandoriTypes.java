package com.AkoBot.Bandori;

import com.AkoBot.Music.Song;

import java.awt.*;

public class BandoriTypes {
    public Color getColor(BandType band) {
        switch (band) {
            case RAS:
                return new Color(0x07ABAD);
            case POPIPA:
                return new Color(0xFF3B72);
            case ROSELIA:
                return new Color(0x5B67FF);
            case GURIGURI:
                return new Color(0x01DFA5);
            case PASUPARE:
                return new Color(0x2AF6B1);
            case AFTERGLOW:
                return new Color(0xE53343);
            case ARGONAVIS:
                return new Color(0x00A0FF);
            case HELLOHAPPY:
                return new Color(0xFFC02A);
            case CHARACTERSONG:
                return new Color(0xBA00BA);
            case CHISPA:
                return new Color(0xBA00BA);
            case GFRIEND:
                return new Color(0xBA00BA);
            case HSPTW:
                return new Color(0xBA00BA);
            default:
                return null;
        }
    }
    public int getMemberId(String string) {
        string = string.toLowerCase();
        switch (string) {
            //Roselia
            case "shirokane rinko":
            case "rinko shirokane":
                return 30;
            case "ako udagawa":
            case "udagawa ako":
                return 29;
            case "yukina minato":
            case "minato yukina":
                return 26;
            case "sayo hikawa":
            case "hikawa sayo":
                return 27;
            case "lisa imai":
            case "imai lisa":
                return 28;
            //Afterglow
            case "ran mitake":
            case "mitake ran":
                return 11;
            case "moca aoba":
            case "aoba moca":
                return 12;
            case "tomoe udagawa":
            case "udagawa tomoe":
                return 14;
            case "himari uehara":
            case "uehara himari":
                return 13;
            case "tsugumi hazawa":
            case "hazawa tsugumi":
                return 15;

            //Hello, Happy World
            case "hagumi kitazawa":
            case "kitazawa hagumi":
                return 18;
            case "kanon matsubara":
            case "matsubara kanon":
                return 19;
            case "kokoro tsurumaki":
            case "tsurumaki kokoro":
                return 16;
            case "kaoru seta":
            case "seta kaoru":
                return 17;
            case "misaki okusawa":
            case "okusawa misaki":
                return 20;

            //Pastel Palettes
            case "hina hikawa":
            case "hikawa hina":
                return 22;
            case "aya maruyama":
            case "maruyama aya":
                return 21;
            case "eve wakamiya":
            case "wakamiya eve":
                return 25;
            case "chisato shirasagi":
            case "shirasagi chisato":
                return 23;
            case "maya yamato":
            case "yamato maya":
                return 24;

            //Poppin' Party
            case "tae hanazono":
            case "hanazono tae":
                return 7;
            case "arisa ichigaya":
            case "ichigaya arisa":
                return 10;
            case "toyama kasumi":
            case "kasumi toyama":
                return 6;
            case "rimi ushigome":
            case "ushigome rimi":
                return 8;
            case "saaya yamabuki":
            case "yamabuki saaya":
                return 9;

            default:
                return -1;
        }
    }
    public MemberType getMemberType(int id) {
        switch (id) {
            //Roselia
            case 30:
                return MemberType.SHIROKANE_RINKO;
            case 29:
                return MemberType.UDAGAWA_AKO;
            case 28:
                return MemberType.IMAI_LISA;
            case 27:
                return MemberType.HIKAWA_SAYO;
            case 26:
                return MemberType.MINATO_YUKINA;

            //Pastel Palettes
            case 25:
                return MemberType.WAKAMIYA_EVE;
            case 24:
                return MemberType.YAMATO_MAYA;
            case 23:
                return MemberType.SHIRASAGI_CHISATO;
            case 22:
                return MemberType.HIKAWA_HINA;
            case 21:
                return MemberType.MARUYAMA_AYA;

            //Hello, Happy World
            case 20:
                return MemberType.OKUSAWA_MISAKI;
            case 19:
                return MemberType.MATSUBARA_KANON;
            case 18:
                return MemberType.KITAZAWA_HAGUMI;
            case 17:
                return MemberType.SETA_KAORU;
            case 16:
                return MemberType.TSURUMAKI_KOKORO;

            //Afterglow
            case 15:
                return MemberType.HAZAWA_TSUGUMI;
            case 14:
                return MemberType.UDAGAWA_TOMOE;
            case 13:
                return MemberType.UEHARA_HIMARI;
            case 12:
                return MemberType.AOBA_MOCA;
            case 11:
                return MemberType.MITAKE_RAN;

            //Poppin'Party
            case 10:
                return MemberType.KASUMI_TOYAMA;
            case 9:
                return MemberType.YAMABUKI_SAAYA;
            case 8:
                return MemberType.USHIGOME_RIMI;
            case 7:
                return MemberType.HANAZONO_TAE;
            case 6:
                return MemberType.KASUMI_TOYAMA;
            default:
                return null;
        }
    }
    public MemberType getMemberType(String string) {
        string = string.toLowerCase();
        switch (string) {
            //Roselia
            case "shirokane rinko":
            case "rinko shirokane":
                return MemberType.SHIROKANE_RINKO;
            case "ako udagawa":
            case "udagawa ako":
                return MemberType.UDAGAWA_AKO;
            case "yukina minato":
            case "minato yukina":
                return MemberType.MINATO_YUKINA;
            case "sayo hikawa":
            case "hikawa sayo":
                return MemberType.HIKAWA_SAYO;
            case "lisa imai":
            case "imai lisa":
                return MemberType.IMAI_LISA;
            //Afterglow
            case "ran mitake":
            case "mitake ran":
                return MemberType.MITAKE_RAN;
            case "moca aoba":
            case "aoba moca":
                return MemberType.AOBA_MOCA;
            case "tomoe udagawa":
            case "udagawa tomoe":
                return MemberType.UDAGAWA_TOMOE;
            case "himari uehara":
            case "uehara himari":
                return MemberType.UEHARA_HIMARI;
            case "tsugumi hazawa":
            case "hazawa tsugumi":
                return MemberType.HAZAWA_TSUGUMI;

            //Hello, Happy World
            case "hagumi kitazawa":
            case "kitazawa hagumi":
                return MemberType.KITAZAWA_HAGUMI;
            case "kanon matsubara":
            case "matsubara kanon":
                return MemberType.MATSUBARA_KANON;
            case "kokoro tsurumaki":
            case "tsurumaki kokoro":
                return MemberType.TSURUMAKI_KOKORO;
            case "kaoru seta":
            case "seta kaoru":
                return MemberType.SETA_KAORU;
            case "misaki okusawa":
            case "okusawa misaki":
                return MemberType.OKUSAWA_MISAKI;

            //Pastel Palettes
            case "hina hikawa":
            case "hikawa hina":
                return MemberType.HIKAWA_HINA;
            case "aya maruyama":
            case "maruyama aya":
                return MemberType.MARUYAMA_AYA;
            case "eve wakamiya":
            case "wakamiya eve":
                return MemberType.WAKAMIYA_EVE;
            case "chisato shirasagi":
            case "shirasagi chisato":
                return MemberType.SHIRASAGI_CHISATO;
            case "maya yamato":
            case "yamato maya":
                return MemberType.YAMATO_MAYA;

            //Poppin' Party
            case "tae hanazono":
            case "hanazono tae":
                return MemberType.HANAZONO_TAE;
            case "arisa ichigaya":
            case "ichigaya arisa":
                return MemberType.ICHIGAYA_ARISA;
            case "toyama kasumi":
            case "kasumi toyama":
                return MemberType.KASUMI_TOYAMA;
            case "rimi ushigome":
            case "ushigome rimi":
                return MemberType.USHIGOME_RIMI;
            case "saaya yamabuki":
            case "yamabuki saaya":
                return MemberType.YAMABUKI_SAAYA;

            default:
                return null;
        }
    }

    public int getAttributeColor(String attribute) {
        switch (attribute) {
            case "Pure":
                return 0x3ECC24;
            case "Happy":
                return 0xFF8302;
            case "Cool":
                return 0xF72C53;
            case "Powerful":
                return 0x3E5BD3;
            default:
                return 0x000000;
        }
    }
    public String getBandIcon(String bandname) {
        switch (bandname) {
            case ("Poppin'Party") :
                return "https://vignette.wikia.nocookie.net/bandori/images/1/1f/PoPiPa_icon.png/revision/latest?cb=20180522125930";
            case ("Hello, Happy World!") :
                return "https://vignette.wikia.nocookie.net/bandori/images/5/52/HaroHapi_icon.png/revision/latest?cb=20180522125928";
            case ("Afterglow") :
                return "https://vignette.wikia.nocookie.net/bandori/images/0/01/Afterglow_icon.png/revision/latest?cb=20180522125931";
            case ("Roselia") :
                return "https://vignette.wikia.nocookie.net/bandori/images/d/db/Roselia_icon.png/revision/latest?cb=20180522125933";
            case ("Pastel*Palettes") :
                return "https://vignette.wikia.nocookie.net/bandori/images/0/0b/PasuPare_icon.png/revision/latest?cb=20180522125926";
            default:
                return "https://vignette.wikia.nocookie.net/bandori/images/8/84/Ako_PICO_Icon.png/revision/latest?cb=20180715113757";
        }
    }
    public Color getColor(MemberType member) {
        switch (member) {
            //Afterglow
            case MITAKE_RAN:
                return new Color(0xe50028 );
            case AOBA_MOCA:
                return new Color(0x50aac7 );
            case UDAGAWA_TOMOE:
                return new Color(0xa00000);
            case UEHARA_HIMARI:
                return new Color(0xffa0b4 );
            case HAZAWA_TSUGUMI:
                return new Color(0xffdc5a);

            //Argonavis, no member has a color
            //use band color
            case KIKYOU_RIO:
            case GORYOU_YUUTO:
            case MATOBA_WATARU:
            case SHIROISHI_BANRI:
            case NANAHOSHI_REN:
                return new Color(0x00A0FF);

            //Glittler*Green, no member has a color
            //use band color
            case UZAWA_RII:
            case USHIGOME_YURI:
            case WANIBE_NANANA:
            case NIJIKKI_HINAKO:
                return new Color(0x01DFA5);

            //Hello, Happy World
            case KITAZAWA_HAGUMI:
                return new Color(0xfe6d00);
            case MATSUBARA_KANON:
                return new Color(0x00d7ff);
            case TSURUMAKI_KOKORO:
                return new Color(0xffe600);
            case SETA_KAORU:
                return new Color(0xa441b9);
            case OKUSAWA_MISAKI:
                return new Color(0xe545b3);

            //Pastel Palettes
            case HIKAWA_HINA:
                return new Color(0x95e5e5);
            case MARUYAMA_AYA:
                return new Color(0xffc1dc);
            case WAKAMIYA_EVE:
                return new Color(0xc7ccfe);
            case SHIRASAGI_CHISATO:
                return new Color(0xffefaa);
            case YAMATO_MAYA:
                return new Color(0x95e5b3);

            //Poppin' Party
            case HANAZONO_TAE:
                return new Color(0x0000ff);
            case ICHIGAYA_ARISA:
                return new Color(0x800080);
            case KASUMI_TOYAMA:
                return new Color(0xff0000);
            case USHIGOME_RIMI:
                return new Color(0xffc0cb);
            case YAMABUKI_SAAYA:
                return new Color(0xffff00);

            //RAISE A SUILEN
            case WAKANA_REI:
                return new Color(0xCD0A10);
            case ASAHI_ROKKA:
                return new Color(0xBCFF64);
            case SATO_MASUKI:
                return new Color(0xE1BA39);
            case TAMADE_CHIYU:
                return new Color(0x33CCFF);
            case NYUBARA_REONA:
                return new Color(0xFF99CC);

            //Roselia
            case IMAI_LISA:
                return new Color(0xc8463b);
            case HIKAWA_SAYO:
                return new Color(0x32aabe);
            case UDAGAWA_AKO:
                return new Color(0xfa5ab4);
            case MINATO_YUKINA:
                return new Color(0x5a46a9);
            case SHIROKANE_RINKO:
                return new Color(0xa9b3b3);

            //default
            default:
                return null;
        }

    }
    public String getBandString(BandType bandType) {
        switch (bandType) {
            case HELLOHAPPY:
                return "Hello, Happy World!";
            case ARGONAVIS:
                return "Argonavis";
            case AFTERGLOW:
                return "Afterglow";
            case PASUPARE:
                return "Pastel☆Palettes";
            case GURIGURI:
                return "Glitter☆Green";
            case ROSELIA:
                return "Roselia";
            case POPIPA:
                return "Poppin'Party";
            case RAS:
                return "RAISE A SUILEN";
            case GFRIEND:
                return "GFRIEND";
            case CHISPA:
                return "CHiSPA";
            case CHARACTERSONG:
                return "Character Song";
            case HSPTW:
                return "High School Part Time Workers";
            default:
                return null;
        }
    }
    public String getSongString(SongType songType) {
        if (songType == SongType.GAME_VERSION)
            return "Game";
        else if (songType  == SongType.TV_SIZE)
            return "TV Size";
        else if (songType == SongType.ACOUSTIC_INSTRUMENTAL)
            return "Acoustic Instrumental";
        else if (songType == SongType.POPIPA_ACOUSTIC)
            return "Popipa Acoustic";
        else if (songType == SongType.ACOUSTIC)
            return "Acoustic";
        else if (songType == SongType.INSTRUMENTAL)
            return "Instrumental";
        else if (songType == SongType.FULL_VERSION)
            return "Full";
        else if (songType == SongType.ENCORE_LIVE)
            return "THE THIRD 1st Live encore";
        else if (songType == SongType.REMASTER_INSTRUMENTAL)
            return "Remastered Instrumental";
        else if (songType == SongType.REMASTER)
            return "Remastered";
        else if (songType == SongType.SOLO)
            return "Solo";
        else if (songType == SongType.SHORT)
            return "Short";
        return null;
    }
    public BandType getBandType(String bandType) {
        BandoriSongs bandoriSongs = new BandoriSongs();
        bandType = bandoriSongs.removeSpecial(bandType);
        switch (bandType) {
            case "poppinparty":
            case "popipa":
            case "ppp":
                return BandType.POPIPA;
            case "afterglow":
                return BandType.AFTERGLOW;
            case "hellohappyworld":
            case "hellohappy":
            case "harohapi":
                return BandType.HELLOHAPPY;
            case "glittergreen":
            case "guriguri":
                return BandType.GURIGURI;
            case "argonavis":
                return BandType.ARGONAVIS;
            case "raiseasuilen":
            case "ras":
                return BandType.RAS;
            case "roselia":
                return BandType.ROSELIA;
            case "pastelpalettes":
            case "pasupare":
                return BandType.PASUPARE;
            case "gfriend":
                return BandType.GFRIEND;
            case "chispa":
                return BandType.CHISPA;
            case "charactersong":
                return BandType.CHARACTERSONG;
            case "highschoolparttimeworkers":
            case "hsptw":
            case "hsptb":
            case "parttime":
            case "parttimeworkers":
            case "worker":
            case "workers":
            case "parttimers":
                return BandType.HSPTW;
            default:
                return null;
        }
    }
    public SongType getSongType(String message) {
        BandoriSongs bandoriSongs = new BandoriSongs();
        message = bandoriSongs.removeSpecial(message);
        if (message.contains("game") || message.contains("gv"))
            return SongType.GAME_VERSION;
        else if (message.contains("tv"))
            return SongType.TV_SIZE;
        else if (message.contains("acousticinstr") || message.contains("acinstr"))
            return SongType.ACOUSTIC_INSTRUMENTAL;
        else if (message.contains("popipaacoustic"))
            return SongType.POPIPA_ACOUSTIC;
        else if (message.contains("acoustic") || message.equals("ac"))
            return SongType.ACOUSTIC;
        else if (message.contains("instr"))
            return SongType.INSTRUMENTAL;
        else if (message.contains("full"))
            return SongType.FULL_VERSION;
        else if (message.contains("encorelive"))
            return SongType.ENCORE_LIVE;
        else if (message.contains("live"))
            return SongType.LIVE;
        else if (message.contains("masterinstr"))
            return SongType.REMASTER_INSTRUMENTAL;
        else if (message.contains("master"))
            return SongType.REMASTER;
        else if (message.contains("solo"))
            return SongType.SOLO;
        else if (message.contains("short"))
            return SongType.SHORT;
        return null;
    }
}
