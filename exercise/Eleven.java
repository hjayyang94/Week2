import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Eleven
{
    static WordFrequencyController wfcontroller;

    public static void main(String[] args) {
        wfcontroller = new WordFrequencyController(args[0]);
        wfcontroller.dispatch(Arrays.asList("run"));
    }
}
class DataStorageManager{
    private String data = "";

    DataStorageManager(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                data = data.concat(line + " ");
            }

        } catch (Exception e) {
            System.out.println("Could not load file path");
        }
    }

    private List<String> words() {
        String data_str = "".concat(data);
        List<String> list_data = new ArrayList<>(Arrays.asList(data_str.trim().split("[^A-Za-z]")));
        list_data.replaceAll(String::toLowerCase);
        return list_data;
    }

    public List<String> dispatch(List<String> msg) {
        if (msg.get(0).equals("words")) {
            return words();
        }
        return null;

    }
}

class StopWordsManager {
    private List<String> stopWords = new ArrayList<>();

    StopWordsManager() {
        try {
            BufferedReader readStop = new BufferedReader(new FileReader("../stop_words.txt"));
            String line;

            while ((line = readStop.readLine()) != null) {
                stopWords = Arrays.asList(line.split(","));
            }
        } catch (Exception e) {
            System.out.println("Could not load Stop Words");
        }
    }

    private Boolean isStopWord(String word) {
        return stopWords.contains(word);
    }

    public Boolean dispatch(List<String> msg) {
        if (msg.get(0).equals("is_stop_word")) {
            return isStopWord(msg.get(1));
        }
        return null;

    }
}

class WordFrequencyManager {
    Map<String, Integer> map = new HashMap<String, Integer>();
    List<Map.Entry<String, Integer>> sortedList;

    private void incrementCount(String word) {
        if (!map.containsKey(word)) {
            map.put(word, 1);
        } else {
            int currentAmount = map.get(word);
            map.replace(word, currentAmount + 1);
        }
    }

    private void sort() {
        sortedList = new LinkedList<Map.Entry<String, Integer>>(this.map.entrySet());

        Collections.sort(sortedList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
    }

    public List<Map.Entry<String, Integer>> dispatch(List<String> msg) {
        if (msg.get(0).equals("increment_count") && msg.get(1).length() > 1) {
            this.incrementCount(msg.get(1));
        } else if (msg.get(0).equals("sorted")) {
            this.sort();
            return this.sortedList;
        }
        return null;

    }
}

class WordFrequencyController {
    DataStorageManager storageManager;
    StopWordsManager stopWordsManager;
    WordFrequencyManager wordFrequencyManager;

    public WordFrequencyController(String filePath) {
        storageManager = new DataStorageManager(filePath);
        stopWordsManager = new StopWordsManager();
        wordFrequencyManager = new WordFrequencyManager();
    }

    private void run(){
        for (String word : this.storageManager.dispatch(Arrays.asList("words"))){
            if (!this.stopWordsManager.dispatch(Arrays.asList("is_stop_word",word))){
                this.wordFrequencyManager.dispatch(Arrays.asList("increment_count",word));
            }
        }
        List<Map.Entry<String,Integer>> wordFreqs = this.wordFrequencyManager.dispatch(Arrays.asList("sorted"));
        
        int counter = 0;
        for (Map.Entry<String, Integer> entry : wordFreqs){
            System.out.println(entry.getKey()+":"+entry.getValue());
            counter++;
            if(counter > 24){ break; }
        };
    }

    public void dispatch(List<String> msg){
        if (msg.get(0).equals("run")){
            this.run();
        }
    }
}
