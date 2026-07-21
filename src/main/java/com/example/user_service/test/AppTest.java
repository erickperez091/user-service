package com.example.user_service.test;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */

class Song {
    String songName;
    String authorName;
    int playsCount;

    public Song() {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName( String songName ) {
        this.songName = songName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName( String authorName ) {
        this.authorName = authorName;
    }

    public int getPlaysCount() {
        return playsCount;
    }

    public void setPlaysCount( int playsCount ) {
        this.playsCount = playsCount;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", playsCount=" + playsCount +
                '}';
    }
}

public class AppTest {
    //Set< JSONObject > songs = new HashSet<>();
    List< Song > songsList = new ArrayList<>();

    /**
     * Rigorous Test :-)
     */

    public void testWeekOfTheYear() {
        Calendar calendar = Calendar.getInstance();
        System.out.println( calendar.get( Calendar.WEEK_OF_YEAR ) );
    }

    public static void setUp() throws Exception {
        /*JSONObject song1 = new JSONObject();
        song1.put( "songName", "Song Name 1" );
        song1.put( "authorName", "Author 1" );
        song1.put( "playsCount", 0 );
        songs.add( song1 );

        JSONObject song2 = new JSONObject();
        song2.put( "songName", "Song Name 2" );
        song2.put( "authorName", "Author 1" );
        song2.put( "playsCount", 0 );
        songs.add( song2 );

        JSONObject song3 = new JSONObject();
        song3.put( "songName", "Song Name 3" );
        song3.put( "authorName", "Author 2" );
        song3.put( "playsCount", 0 );
        songs.add( song3 );

        JSONObject song4 = new JSONObject();
        song4.put( "songName", "Song Name 4" );
        song4.put( "authorName", "Author 2" );
        song4.put( "playsCount", 0 );
        songs.add( song4 );*/
    }

    public void test() throws JSONException {
        playSong( "Author 2", "Song Name 4" );
        playSong( "Author 2", "Song Name 4" );
        playSong( "Author 2", "Song Name 4" );

        playSong( "Author 3", "Song Name 1" );
        playSong( "Author 1", "Song Name 1" );
        playSong( "Author 1", "Song Name 1" );
        playSong( "Author 1", "Song Name 2" );

        System.out.println( songsList.toString() );
        System.out.println( getTopSong( "Author 1" ) );
        System.out.println( getTopSong( "Author 2" ) );
        System.out.println( getTopSong( "Author 3" ) );
    }

    private String getTopSong( String authorName ) throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append( "Top Song for " ).append( authorName ).append( " is " );
        if ( !songsList.isEmpty() ) {
            sb.append( songsList.stream()
                    .filter( song -> song.getAuthorName().equalsIgnoreCase( authorName ) )
                    .max( Comparator.comparingInt( Song::getPlaysCount ) )
                    .get().getSongName() );

        } else {
            sb.append( "Songs Not Found for that Artist" );
        }
        return sb.toString();

        /*return songs.stream()
                .filter( jsonObject -> {
                    try {
                        return jsonObject.getString( "authorName" ).equalsIgnoreCase( authorName );
                    } catch ( JSONException e ) {
                        throw new RuntimeException( e );
                    }
                } )
                .max( ( o1, o2 ) -> {
                    try {
                        return Integer.compare( o1.getInt( "playsCount" ), o2.getInt( "playsCount" ) );
                    } catch ( JSONException e ) {
                        throw new RuntimeException( e );
                    }
                } ).get().getString( "songName" );
         */
    }

    private void playSong( String authorName, String songName ) throws JSONException {

        Song newSong = new Song();
        newSong.setAuthorName( authorName );
        newSong.setSongName( songName );
        newSong.setPlaysCount( 1 );

        songsList.stream()
                .filter( song -> song.getAuthorName().equalsIgnoreCase( newSong.getAuthorName() ) && song.getSongName().equalsIgnoreCase( newSong.getSongName() ) )
                .findFirst()
                .ifPresentOrElse( song -> song.setPlaysCount( song.getPlaysCount() + 1 ), () -> songsList.add( newSong ) );
/*
        Optional< JSONObject > result = this.songs
                .stream()
                .filter( jsonObject -> {
                    try {
                        return jsonObject.getString( "authorName" ).equalsIgnoreCase( authorName );
                    } catch ( JSONException e ) {
                        throw new RuntimeException( e );
                    }
                } )
                .filter( jsonObject -> {
                    try {
                        return jsonObject.getString( "songName" ).equalsIgnoreCase( songName );
                    } catch ( JSONException e ) {
                        throw new RuntimeException( e );
                    }
                } )
                .map( jsonObject -> {
                    try {
                        jsonObject.put( "playsCount", jsonObject.getInt( "playsCount" ) + 1 );
                    } catch ( JSONException e ) {
                        throw new RuntimeException( e );
                    }
                    return jsonObject;
                } ).findFirst();
        songs.add( result.orElse( new JSONObject().put( "authorName", authorName ).put( "songName", songName ).put( "playsCount", 1 ) ) ); */
    }


    //@ValueSource( strings = { "abcdeabcd", "abcdeabcdef" } )
    public void shortestNonRepeatedString( String input ) {
        int start = 0;
        int end = 0;
        int currentStart = 0;
        int currentEnd = 0;
        Map< Character, Integer > map = new HashMap<>();
        for ( int i = 0; i < input.length(); i++ ) {
            Character c = input.charAt( i );
            if ( !map.containsKey( c ) ) {
                map.put( c, 1 );
                currentEnd = i + 1;
            } else {
                start = currentStart;
                end = currentEnd;
                currentStart = currentEnd;
                map.clear();
            }
        }
        if ( currentEnd - currentStart < end - start ) {
            end = currentEnd;
            start = currentStart;
        }
        System.out.println( input.substring( start, end ) );
    }

    public void enoughMoney() {
        List< List< Integer > > peopleQueues = getLists();
        for(List< Integer > peopleQueue : peopleQueues) {
            int amount_25 = 0;
            int amount_50 = 0;
            String result = hasEnoughMoney( peopleQueue, amount_25, amount_50 );
            System.out.println( result );
        }


        /*inputs.forEach( input -> {
                    AtomicReference< String > result = new AtomicReference<>( "YES" );
                    AtomicInteger amountOfMoneys_25 = new AtomicInteger();
                    AtomicInteger amountOfMoneys_50 = new AtomicInteger();
                    input.forEach( element -> {
                        if ( element == 25 ) {
                            amountOfMoneys_25.getAndIncrement();
                        } else if ( element == 50 ) {
                            if ( amountOfMoneys_25.get() > 0 ) {
                                amountOfMoneys_50.getAndIncrement();
                                amountOfMoneys_25.getAndDecrement();
                            } else {
                                result.set( "NO" );
                            }
                        } else if ( element == 100 ) {
                            if ( amountOfMoneys_50.get() == 0 ) {
                                int aux25 = amountOfMoneys_25.get();
                                amountOfMoneys_25.set( aux25 - 3 );
                            } else {
                                amountOfMoneys_25.getAndDecrement();
                                amountOfMoneys_50.getAndDecrement();
                            }
                        }
                        if ( amountOfMoneys_25.get() < 0 || amountOfMoneys_50.get() < 0 ) {
                            result.set( "NO" );
                        }
                    } );
                    System.out.println( result.get() );
                }
        );*/
    }

    private String hasEnoughMoney(List<Integer> peopleQueue, int amount_25, int amount_50) {
        for(Integer personBill : peopleQueue) {
            if ( personBill == 25 ) {
                amount_25++;
            } else if ( personBill == 50 ) {
                if ( amount_25 > 0 ) {
                    amount_50++;
                    amount_25--;
                } else {
                    return "NO";
                }
            } else if (personBill == 100 ) {
                if( amount_25 > 0 && amount_50 > 0) {
                    amount_50--;
                    amount_25--;
                } else if(amount_25 < 3){
                    return "NO";
                } else {
                    amount_25 -= 3;
                }
            }
            if(amount_25 < 0 || amount_50 < 0 ) {
                return "NO";
            }
        }
        return "YES";
    }

    private static List< List< Integer > > getLists() {
        List< List< Integer > > inputs = new ArrayList<>();
        List< Integer > input1 = Arrays.asList( 25, 25, 50 );
        List< Integer > input2 = Arrays.asList( 25, 100 );
        List< Integer > input3 = Arrays.asList( 25, 25, 50, 50, 100 );
        List< Integer > input4 = Arrays.asList( 25, 25, 50, 50, 25, 100 );
        List< Integer > input5 = Arrays.asList( 25, 25, 25, 100 );

        inputs.add( input1 );
        inputs.add( input2 );
        inputs.add( input3 );
        inputs.add( input4 );
        inputs.add( input5 );
        return inputs;
    }


    //@ValueSource(strings = {"abcabcbb", "bbbbb", "pwwkew", "abcabcbbabcd"})
    //@ValueSource( strings = { "abcabcbbabcd" } )
    //@ValueSource( strings = { "abcdeabcd" } )
    public void longestSubstringTest( String s ) {
        System.out.printf( "Substring: %s \n ", longestSubstring( s ) );
        System.out.println( "---------------------------------------" );
    }

    private String longestSubstring( String s ) {
        int start = 0;
        int end = 0;
        int startAux = 0;
        int endAux = 0;
        int maxLength = 0;
        Map< Character, Integer > map = new HashMap<>();
        for ( int i = 0; i < s.length(); i++ ) {
            Character character = s.charAt( i );
            if ( !map.containsKey( character ) ) {
                endAux = i + 1;
            } else {
                String substring = s.substring( startAux, endAux );
                int currentLength = substring.length();
                if ( currentLength > maxLength ) {
                    start = startAux;
                    end = endAux;
                    maxLength = end - start;
                }
                startAux = endAux;
                endAux++;
                map.clear();
            }
            map.put( character, i );
            if ( i == s.length() - 1 && endAux - startAux > maxLength ) {
                start = startAux;
                end = endAux;
                maxLength = end - start;
            }
        }
        System.out.printf( "Start: %s, End: %s%n", start, ( end - 1 ) );
        System.out.println( "max_length = " + maxLength );
        return s.substring( start, end );
    }


	/*
	Given a string s, find the length of the longest
substring without repeating characters.

Example 1:

Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3.

Example 2:

Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.

Example 3:

Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
	* */

    public void longest_palindromic_test() {

        System.out.println( longestPalindromic( "babad" ) );
        System.out.println( longestPalindromic( "abcbabcbabcba" ) );
        System.out.println( longestPalindromic( "forgeeksskeegfor" ) );
        System.out.println( longestPalindromic( "abaaba" ) );
    }

    private String longestPalindromic( String s ) {
        if ( s.length() < 2 ) {
            return s;
        }
        for ( int i = 0; i < s.length(); i++ ) {
            for ( int j = s.length() - 1; j > 0; j-- ) {
                String substring = s.substring( i, j + 1 );
                if ( substring.length() > 1 && isPalindrome( substring ) ) {
                    return s.substring( i, j + 1 );
                }
            }
        }
        return s;
    }

    private boolean isPalindrome( String s ) {
        StringBuilder rev = new StringBuilder();
        boolean ans = false;
        for ( int i = s.length() - 1; i >= 0; i-- ) {
            rev.append( s.charAt( i ) );
        }
        if ( s.contentEquals( rev ) ) {
            ans = true;
        }
        return ans;
    }

    public static void main(String[] args) {
        AppTest test = new AppTest();
        System.out.println("------------------------------- [START] ENOUGH MONEY SECTION -------------------------------");

        test.enoughMoney();

        System.out.println("------------------------------- [END ]ENOUGH MONEY SECTION -------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("------------------------------- [START] PLAY SONG SECTION -------------------------------");

        test.playSong( "Author 2", "Song Name 4" );
        test.playSong( "Author 2", "Song Name 4" );
        test.playSong( "Author 2", "Song Name 4" );

        test.playSong( "Author 3", "Song Name 1" );
        test.playSong( "Author 1", "Song Name 1" );
        test.playSong( "Author 1", "Song Name 1" );
        test.playSong( "Author 1", "Song Name 2" );
        Map<String, Long> counter = test.songsList.stream().collect(Collectors.groupingBy(Song::getSongName, Collectors.counting()));
        System.out.println(" Songs by Author :" + counter);
        System.out.println( test.songsList.toString() );
        System.out.println( test.getTopSong( "Author 1" ) );
        System.out.println( test.getTopSong( "Author 2" ) );
        System.out.println( test.getTopSong( "Author 3" ) );

        System.out.println("------------------------------- [END] PLAY SONG SECTION -------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("------------------------------- [START] LONGEST SUBSTRING SECTION -------------------------------");

        test.longestSubstringTest("abcabcbb");
        test.longestSubstringTest("bbbbb");
        test.longestSubstringTest("pwwkew");
        test.longestSubstringTest("abcabcbbabcd");
        test.longestSubstringTest("abcdeabcd");

        System.out.println("------------------------------- [END] LONGEST SUBSTRING SECTION -------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("------------------------------- [START] LONGEST PALINDROME SECTION -------------------------------");

        test.longest_palindromic_test();

        System.out.println("------------------------------- [END] LONGEST PALINDROME SECTION -------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("------------------------------- [START] SHORTEST NON REPEATED STRING SECTION -------------------------------");

        test.shortestNonRepeatedString("abcdeabcd");
        test.shortestNonRepeatedString("abcdeabcdef");

        System.out.println("------------------------------- [END] SHORTEST NON REPEATED STRING SECTION -------------------------------");
    }
}
