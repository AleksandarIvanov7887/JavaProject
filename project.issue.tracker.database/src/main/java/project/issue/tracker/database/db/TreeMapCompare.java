package project.issue.tracker.database.db;

import java.util.Comparator;

final class TreeMapCompare implements Comparator< String> {
	static TreeMapCompare instance = null;
	
	public static TreeMapCompare getInstance() {
		if (instance == null) {
			if (instance == null) {
				instance = new TreeMapCompare();
			}
		}
		return instance;
	}
	
	private TreeMapCompare() {
		
	}

    char first, second;
    int key1_length, key2_length;

    public int compare(final String key1, final String key2) {
        if (key1.charAt(0) != '#' && key2.charAt(0) == '#') {
            return -1;
        }
        if (key2.charAt(0) != '#' && key1.charAt(0) == '#') {
            return 1;
        }
        key1_length = key1.length();
        key2_length = key2.length();
        if (key1_length < key2_length) {
            return -1;
        } else if (key1_length > key2_length) {
            return 1;
        } else {
            for (int i = 0; i < key1_length; ++i) {
                first = key1.charAt(i);
                second = key2.charAt(i);
                if (first == second) {
                    continue;
                }
                if (first < second) {
                    return -1;
                } else {
                    return 1;
                }
            }

        }
        return 0;
    }
}