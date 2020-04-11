import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Four
{
    private static String file_path;
    private static List<String> stop_words;
    private static String data = "";
    private static List<String> list_data;
    private static String stop_path;
    private static HashMap<String, Integer> map = new HashMap<>();
    private static List<Map.Entry<String, Integer>> results;
    
    public static void main( String[] args )
    {
        generate_stop_words();
        read_file(args[0]);
        convert_data_to_list();
        filter_list();
        populate_map();
        sort_map_to_list();
        print_results();
    }

    private static void generate_stop_words(){
        try{
            BufferedReader readStop = new BufferedReader(new FileReader("../stop_words.txt"));
            String line;
            while ((line = readStop.readLine()) != null) {
                stop_words = Arrays.asList(line.split(","));
            }

        }
        catch(Exception e){
            System.out.println("Could not load Stop Words");
        }
    }

    private static void read_file(String filePath){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = reader.readLine()) != null){
                data = data.concat(line+" ");
            }
        }
        catch(Exception e){
            System.out.println("Could not load file path");
        }
    }

    private static void convert_data_to_list(){
        list_data = new ArrayList<>(Arrays.asList(data.trim().split("[^A-Za-z]")));
        list_data.replaceAll(String::toLowerCase);
    }

    private static void filter_list(){
        
        try{
            BufferedReader readStop = new BufferedReader(new FileReader("../stop_words.txt"));
            String line;
            while ((line = readStop.readLine()) != null) {
                stop_words = Arrays.asList(line.split(","));
            }

        }
        catch(Exception e){
            System.out.println("Could not load Stop Words");
        }


        for (Iterator<String> iter = list_data.listIterator(); iter.hasNext(); ){
            String word = iter.next();
            if(word.length() < 2 || stop_words.contains(word)){
                iter.remove();
            }
        }
    }

    private static void populate_map(){
        for(Iterator<String> iter = list_data.listIterator(); iter.hasNext(); ){
            String word = iter.next();
            if (!map.containsKey(word)){
                map.put(word,1);
            }
            else{
                int currentAmount = map.get(word);
                map.replace(word, currentAmount+1);
            }
        }
    }

    private static void sort_map_to_list(){
        results = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(results, new Comparator<Map.Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
    }

    private static void print_results(){
        StringBuilder out = new StringBuilder("-----------Results----------");
        int counter = 0;
        for (Map.Entry<String, Integer> entry : results){
            out.append("\n"+entry.getKey()+":"+entry.getValue());
            counter++;
            if(counter > 24){ break; }
        };

        System.out.println(out.toString());
    }


}