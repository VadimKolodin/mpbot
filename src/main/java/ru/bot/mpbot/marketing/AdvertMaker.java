package ru.bot.mpbot.marketing;


public class
AdvertMaker {

    public static String getRandomAdvert(){
        Adverts[] ads = AdvertMaker.Adverts.values();
        int ordinal = (int)(Math.random()*ads.length);
        return ads[ordinal].getText();
    }

    private enum Adverts{
        AD1("Рекалма 1"),
        AD2("Рекалма 2"),
        AD3("Рекалма 3");

        private final String text;

        Adverts(String text){
            this.text=text;
        }

        public String getText(){
            return text;
        }
    }
}
