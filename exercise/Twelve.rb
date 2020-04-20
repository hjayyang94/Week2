def extract_words(obj, path_to_file)
    obj["data"] = File.read(path_to_file).split(/[\W_]+/).map(&:downcase)
end

def load_stop_words(obj)
    obj["stop_words"] = File.read("../stop_words.txt").split(",")
    obj["stop_words"].concat(Array("a".."z"))
end

def increment_count(obj, w)
    if !obj["freqs"].include? w
        obj["freqs"][w] = 1
    else
        obj["freqs"][w] = obj["freqs"][w]+1
    end
end
    

data_storage_obj = {
    "data" => [],
    "init" => lambda { |path_to_file| extract_words(data_storage_obj, path_to_file)},
    "words" => lambda {data_storage_obj["data"]}
}

stop_words_obj = {
    "stop_words" => [],
    "init" => -> {load_stop_words(stop_words_obj)},
    "is_stop_word" => lambda { |word| stop_words_obj["stop_words"].include? word }
}

word_freqs_obj = {
    "freqs" => {},
    "increment_count" => lambda { |w| increment_count(word_freqs_obj, w)},
    "sorted" => lambda { word_freqs_obj["freqs"].sort_by{ |word, count| -count } }

}

data_storage_obj["init"].call(ARGV[0])
stop_words_obj["init"].call

for w in data_storage_obj["words"].call 
    if !stop_words_obj["is_stop_word"].call(w)
        word_freqs_obj["increment_count"].call(w)
    end
end

word_freqs_obj["print"] = lambda do
    word_freqs = word_freqs_obj["sorted"].call
    for (w, c) in word_freqs[0..25]
        puts w+ ' - '+ c.to_s
    end
end

word_freqs_obj["print"].call