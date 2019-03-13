package com.AkoBot.Bandori;

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
            default:
                return null;
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
    public String getBandType(BandType bandType) {
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
            default:
                return null;
        }
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
