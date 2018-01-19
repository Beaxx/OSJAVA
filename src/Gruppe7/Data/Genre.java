package Gruppe7.Data;

    //Define enum
    public enum Genre {

        ACTION("Action"),
        DOKUMENTATION("Dokumentation"),
        DRAMA("Drama"),
        HORROR("Horror"),
        KOMOEDIE("Komödie"),
        KRIMI("Krimi"),
        SCIENCE_FICTION("Science Fiction"),
        THRILLER("Thriller"),
        ZEICHENTRICK("Zeichentrick");


        private String genereName;
        Genre(String genreName){
            this.genereName = genreName;
        }

        public String getGenereName(){
            return this.genereName;
        }
    }



