package sg.edu.tp.whisper;

import java.util.ArrayList;
import java.util.Random;

public class SongCollection {

    SongCollection() {
        prepareTrackList();
        prepareTopTrackList();
        prepareEdSheeranSongList();
        prepareTheCabSongs();
        prepareOneOkRockSongs();
        prepareColdPlaySongs();
        prepareTopArtistes();
        prepareAllSongs();
    }


    private ArrayList<Song> emptyList = new ArrayList<>();
    ArrayList<Song> getEmptyList() {
        return emptyList;
    }

    private ArrayList<Song> songs = new ArrayList<>();

    ArrayList<Song> getSongs() {
        return songs;
    }
    private void prepareTrackList() {
        Song theWayYouLookTonight = new Song("S1001", "The Way You Look Tonight", "Michael Buble",
                "a5b8972e764025020625bbf9c1c2bbb06e394a60?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.michael_buble_collection, getEmptyList());

        Song billieJean = new Song("S1002", "Billie Jean", "Michael Jackson",
                "f504e6b8e037771318656394f532dede4f9bcaea?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.billie_jean, getEmptyList());

        Song photograph = new Song("S1003", "Photograph", "Ed Sheeran",
                "097c7b735ceb410943cbd507a6e1dfda272fd8a8?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.photograph, getEmptyList());

        Song endlessly = new Song("S1004", "Endlessly", "The Cab",
                "99e455921cf33b4242b463f778111cad251c1937?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.endlessly, getEmptyList());

        Song wonderwall = new Song("S1005", "Wonderwall", "Oasis",
                "b4347e755d823bd300c7520a2ab7533a718a7c98?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.wonder_wall, getEmptyList());

        Song lettingGo = new Song("S1006", "Letting Go", "One OK Rock",
                "1cf8d48884a0ce9cbf21bba281a0a8e2b43f0bb2?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.letting_go, getEmptyList());

        Song theScientist = new Song("S1007", "The Scientist", "Coldplay",
                "95cb9df1b056d759920b5e85ad7f9aff0a390671?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.the_scientist, getEmptyList());

        Song boyWithLuv = new Song("S1008", "Boy With Luv", "BTS",
                "d16797fb391fb909f3c46454d7cf89a2718f8171?cid=2afe87a64b0042dabf51f37318616965",  R.drawable.persona, getEmptyList());


        songs.add(theWayYouLookTonight);
        songs.add(billieJean);
        songs.add(photograph);
        songs.add(endlessly);
        songs.add(wonderwall);
        songs.add(lettingGo);
        songs.add(theScientist);
        songs.add(boyWithLuv);


    }



    private ArrayList<Song> topSongs = new ArrayList<>();

    ArrayList<Song> getTopSongs() {
        return topSongs;
    }
    private void prepareTopTrackList() {

        Song photograph = new Song("S1003", "Photograph", "Ed Sheeran",
                "097c7b735ceb410943cbd507a6e1dfda272fd8a8?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.photograph, edSheeranSongs);

        Song endlessly = new Song("S1004", "Endlessly", "The Cab",
                "99e455921cf33b4242b463f778111cad251c1937?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.endlessly, theCabSongs);

        Song lettingGo = new Song("S1006", "Letting Go", "One OK Rock",
                "1cf8d48884a0ce9cbf21bba281a0a8e2b43f0bb2?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.letting_go, oneOkRockSongs);

        Song theScientist = new Song("S1007", "The Scientist", "Coldplay",
                "95cb9df1b056d759920b5e85ad7f9aff0a390671?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.the_scientist, coldPlaySongs);

        topSongs.add(photograph);
        topSongs.add(endlessly);
        topSongs.add(lettingGo);
        topSongs.add(theScientist);

    }

    private ArrayList<Song> topArtistes = new ArrayList<>();

    ArrayList<Song> getTopArtistes() {
        return topArtistes;
    }
    private void prepareTopArtistes() {

        Song edSheeran = new Song("S1003", "", "Ed Sheeran",
                "097c7b735ceb410943cbd507a6e1dfda272fd8a8?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.ed_sheeran, edSheeranSongs);

        Song theCab = new Song("S1004", "", "The Cab",
                "99e455921cf33b4242b463f778111cad251c1937?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.the_cab, theCabSongs);

        Song oneOkRock = new Song("S1006", "", "One OK Rock",
                "1cf8d48884a0ce9cbf21bba281a0a8e2b43f0bb2?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.one_ok_rock, oneOkRockSongs);

        Song coldplay = new Song("S1007", "", "Coldplay",
                "95cb9df1b056d759920b5e85ad7f9aff0a390671?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.coldplay, coldPlaySongs);

        topArtistes.add(edSheeran);
        topArtistes.add(theCab);
        topArtistes.add(oneOkRock);
        topArtistes.add(coldplay);

    }


    private ArrayList<Song> edSheeranSongs = new ArrayList<>();

    ArrayList<Song> getEdSheeranSongs() {return edSheeranSongs; }
    private void prepareEdSheeranSongList() {
        Song photograph = new Song("E1001", "Photograph", "Ed Sheeran",
                "097c7b735ceb410943cbd507a6e1dfda272fd8a8?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.photograph, edSheeranSongs);
        Song one = new Song("E1002", "One", "Ed Sheeran",
                "daa8679253ba20620db6e1db9c88edfcf1f4069f?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.photograph, edSheeranSongs);
        Song dont = new Song("E1003", "Don't", "Ed Sheeran",
                "e42999c81431d6e5432b8cd6b7e2dc4795daf6a7?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.photograph, edSheeranSongs);
        Song touchAndGo = new Song("E1004", "Touch and Go", "Ed Sheeran",
                "d9720b94d983ae9c055a14f944e404861ed2d110?cid=2afe87a64b0042dabf51f37318616965",
                 R.drawable.photograph, edSheeranSongs);
        Song shapeOfYou = new Song("E1005", "Shape of You", "Ed Sheeran",
                "84462d8e1e4d0f9e5ccd06f0da390f65843774a2?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.divide, edSheeranSongs);
        Song happier = new Song("E1006", "Happier", "Ed Sheeran",
                "e2f5edb569c73916428ec0a2e0b56a9f777851dd?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.divide, edSheeranSongs);
        Song saveMyself = new Song("E1007", "Save Myself", "Ed Sheeran",
                "4347957dfa008ba33c58177c860ef54bb4420c9f?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.divide, edSheeranSongs);

        edSheeranSongs.add(photograph);
        edSheeranSongs.add(one);
        edSheeranSongs.add(dont);
        edSheeranSongs.add(touchAndGo);
        edSheeranSongs.add(shapeOfYou);
        edSheeranSongs.add(happier);
        edSheeranSongs.add(saveMyself);
    }

    private ArrayList<Song> theCabSongs = new ArrayList<>();

    ArrayList<Song> getTheCabSongs() {return theCabSongs; }
    private void prepareTheCabSongs() {

        Song endlessly = new Song("C1001", "Endlessly", "The Cab",
                "99e455921cf33b4242b463f778111cad251c1937?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.endlessly, theCabSongs);

        Song temporaryBliss = new Song("C1002", "Temporary Bliss", "The Cab",
                "097c7b735ceb410943cbd507a6e1dfda272fd8a8?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.endlessly, theCabSongs);

        Song animal = new Song("C1003", "Animal", "The Cab",
                "1cd93ad0794238f250d15b6af74d86428564c117?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.endlessly, theCabSongs);

        Song livingLouder = new Song("C1004", "Living Louder", "The Cab",
                "c984f0470f5c4286da8d92127dcb4dbf3a727c13?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.endlessly, theCabSongs);

        Song anotherMe = new Song("C1005", "Another Me", "The Cab",
                "88f0f310ce21d7d9b1c5ee299a29bf3e39ba4d5e?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.endlessly, theCabSongs);

        Song lala = new Song("C1006", "La La", "The Cab",
                "7749b30e422abe2d3bc980943a4cbf5dbbf84a6f?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.endlessly, theCabSongs);

        Song lovesickFool = new Song("C1007", "Lovesick Fool", "The Cab",
                "f33fe9adbabc219d4b3648ac0dd011fcf4b82d11?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.endlessly, theCabSongs);

        theCabSongs.add(endlessly);
        theCabSongs.add(temporaryBliss);
        theCabSongs.add(animal);
        theCabSongs.add(livingLouder);
        theCabSongs.add(anotherMe);
        theCabSongs.add(lala);
        theCabSongs.add(lovesickFool);

    }

    private ArrayList<Song> oneOkRockSongs = new ArrayList<>();

    ArrayList<Song> getOneOkRockSongs() {return theCabSongs; }
    private void prepareOneOkRockSongs() {
        Song lettingGo = new Song("O1001", "Letting Go", "One OK Rock",
                "1cf8d48884a0ce9cbf21bba281a0a8e2b43f0bb2?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.letting_go, oneOkRockSongs);
        Song standOutFitIn = new Song("O1002", "Stand Out Fit In", "One OK Rock",
                "38d0cae6553d95402c1ea5fe3a1e0643c5aba0cd?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.letting_go, oneOkRockSongs);
        Song change = new Song("O1003", "Change", "One OK Rock",
                "6c7125fa1a6bea2570d5d6fea330a54ae00d12f5?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.letting_go, oneOkRockSongs);
        Song inTheStars = new Song("O1004", "In The Stars", "One OK Rock",
                "dd2efeb02e99c7b05647633b13a13a4052476290?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.letting_go, oneOkRockSongs);
        Song growOldDieYoung = new Song("O1005", "Grow Old Die Young", "One OK Rock",
                "45bf8c1461af84cfa6019e2c63f6feef62b8e2fd?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.letting_go, oneOkRockSongs);
        Song eyeOfTheStorm = new Song("O1006", "Eye of the Storm", "One OK Rock",
                "31e503acaf8250b6df695ff2b871abd803fc4385?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.letting_go, oneOkRockSongs);
        Song headHigh = new Song("O1007", "Head High", "One OK Rock",
                "b2f51f047f0f2d92f2929d75e2ebad106a53f277?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.letting_go, oneOkRockSongs);

        oneOkRockSongs.add(lettingGo);
        oneOkRockSongs.add(standOutFitIn);
        oneOkRockSongs.add(change);
        oneOkRockSongs.add(inTheStars);
        oneOkRockSongs.add(growOldDieYoung);
        oneOkRockSongs.add(eyeOfTheStorm);
        oneOkRockSongs.add(headHigh);
    }

    private ArrayList<Song> coldPlaySongs = new ArrayList<>();

    ArrayList<Song> getColdPlaySongs() {return theCabSongs; }
    private void prepareColdPlaySongs() {
        Song theScientist = new Song("C1001", "The Scientist", "Coldplay",
                "95cb9df1b056d759920b5e85ad7f9aff0a390671?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.the_scientist, coldPlaySongs);
        Song amsterdam = new Song("C1002", "Amsterdam", "Coldplay",
                "c48e6d3099d3b6114fdb00ad5da729fe20ba7c16?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.the_scientist, coldPlaySongs);
        Song clocks = new Song("C1003", "Clocks", "Coldplay",
                "24c7fe858b234e3cb21872cd03ab44b669163fbb?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.the_scientist, coldPlaySongs);
        Song daylight = new Song("C1004", "Daylight", "Coldplay",
                "4f024aa4b08ed605c8ce05579d4d0c7629918a00?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.the_scientist, coldPlaySongs);
        Song greenEyes = new Song("C1005", "Green Eyes", "Coldplay",
                "31819a056e48b71d05c54b7745f1256f3ffd927a?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.the_scientist, coldPlaySongs);
        Song aWhisper = new Song("C1006", "A Whisper", "Coldplay",
                "2871daf282eff729ec13638a79b60351c644d497?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.the_scientist, coldPlaySongs);
        Song inMyPlace = new Song("C1007", "In My Place", "Coldplay",
                "e152576c5affbc6d08bb5b67ff0f4d18e29914ce?cid=2afe87a64b0042dabf51f37318616965",
                R.drawable.the_scientist, coldPlaySongs);

        coldPlaySongs.add(theScientist);
        coldPlaySongs.add(amsterdam);
        coldPlaySongs.add(clocks);
        coldPlaySongs.add(daylight);
        coldPlaySongs.add(greenEyes);
        coldPlaySongs.add(aWhisper);
        coldPlaySongs.add(inMyPlace);

    }

    private ArrayList<Song> allSongs = new ArrayList<>();

    ArrayList<Song> getAllSongs() {return allSongs; }
    private void prepareAllSongs() {
        allSongs.addAll(coldPlaySongs);
        allSongs.addAll(oneOkRockSongs);
        allSongs.addAll(edSheeranSongs);
        allSongs.addAll(theCabSongs);
    }
}
