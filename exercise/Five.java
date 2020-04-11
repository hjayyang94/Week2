import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Five
{
    
    public static void main( String[] args )
    {
        print_results(sort_map_to_list( populate_map( filter_list( convert_data_to_list( read_file( args[0] ))))));
    }


    private static String read_file(String filePath){
        String data = "";
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
        return data;
    }

    private static List<String> convert_data_to_list(String data){
        List<String> list_data = new ArrayList<>(Arrays.asList(data.trim().split("[^A-Za-z]")));
        list_data.replaceAll(String::toLowerCase);
        return list_data;
    }

    private static List<String> filter_list(List<String> list_data){
        
        try{
            BufferedReader readStop = new BufferedReader(new FileReader("../stop_words.txt"));
            String line;
            List<String> stop_words = new ArrayList<>();
            while ((line = readStop.readLine()) != null) {
                stop_words = Arrays.asList(line.split(","));
            }

            for (Iterator<String> iter = list_data.listIterator(); iter.hasNext(); ){
                String word = iter.next();
                if(word.length() < 2 || stop_words.contains(word)){
                    iter.remove();
                }
            }
            
        }
        catch(Exception e){
            System.out.println("Could not load Stop Words");
        }

        return list_data;

    }

    private static HashMap<String, Integer> populate_map(List<String> list_data){
        HashMap<String, Integer> map = new HashMap<>();

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
        return map;
    }

    private static List<Map.Entry<String, Integer>> sort_map_to_list(HashMap<String, Integer> map){
        List<Map.Entry<String, Integer>> results = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(results, new Comparator<Map.Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        return results;
    }

    private static void print_results(List<Map.Entry<String,Integer>> results){
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