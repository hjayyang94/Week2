import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Thirteen {
    public static void main(String[] args) {
        TWordFrequencyController wfc = new TWordFrequencyController(args[0]);
        wfc.run();
    }
}

abstract class IDataStorage {
    public abstract List<String> words();
}

abstract class IStopWordFilter {
    public abstract Boolean is_stop_word(String word);
}

abstract class IWordFrequencyCounter {
    public abstract void increment_count(String word);

    public abstract List<Map.Entry<String, Integer>> sorted();
}

class TDataStorageManager extends IDataStorage {
    List<String> wordsL;
    public TDataStorageManager(String path_to_file){
        String data = "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path_to_file));
            String line;
            while((line = reader.readLine()) != null){
                data = data.concat(line+" ");
            }
            wordsL = new ArrayList<>(Arrays.asList(data.trim().split("[^A-Za-z]")));
            wordsL.replaceAll(String::toLowerCase);
        }
        catch(Exception e){
            System.out.println("Could not load file path");
        }
    }
    @Override
    public List<String> words() {
        return this.wordsL;
    }

}

class TStopWordManager extends IStopWordFilter {
    List<String> stopWords;

    public TStopWordManager(){
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
    @Override
    public Boolean is_stop_word(String word) {
        return stopWords.contains(word);
    }

}

class TWordFrequencyManager extends IWordFrequencyCounter {
    Map<String, Integer> map = new HashMap<String, Integer>();
    List<Map.Entry<String, Integer>> sortedList;
    
    @Override
    public void increment_count(String word) {
        if (!map.containsKey(word)) {
            map.put(word, 1);
        } else {
            int currentAmount = map.get(word);
            map.replace(word, currentAmount + 1);
        }
    }

    @Override
    public List<Entry<String, Integer>> sorted() {
        sortedList = new LinkedList<Map.Entry<String, Integer>>(this.map.entrySet());

        Collections.sort(sortedList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        return this.sortedList;
    }

}

class TWordFrequencyController {
    TDataStorageManager storage;
    TStopWordManager stop_word_manager;
    TWordFrequencyManager word_freq_counter;

    public TWordFrequencyController(String path_to_file){
        this.storage = new TDataStorageManager(path_to_file);
        this.stop_word_manager = new TStopWordManager();
        this.word_freq_counter = new TWordFrequencyManager();
    }

    public void run(){
        for (String word : this.storage.words()){
            if (!this.stop_word_manager.is_stop_word(word) && word.length() > 1){
                this.word_freq_counter.increment_count(word);
            }
        }

        List<Entry<String, Integer>> word_freqs = this.word_freq_counter.sorted();

        int counter = 0;
        for (Map.Entry<String, Integer> entry : word_freqs){
            System.out.println(entry.getKey()+":"+entry.getValue());
            counter++;
            if(counter > 24){ break; }
        };
    }
}