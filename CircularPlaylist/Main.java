import java.util.*;

// ========================
// Class Song
// ========================
class Song {
    String title;
    String artist;
    int duration; // in seconds
    Song next;

    Song(String title, String artist, int duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.next = null;
    }

    String formatDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}

// ========================
// Class CircularPlaylist
// ========================
class CircularPlaylist {
    private Song head = null;
    private Song tail = null;
    private Song current = null;

    public void addSong(String title, String artist, int duration) {
        Song newSong = new Song(title, artist, duration);
        if (head == null) {
            head = newSong;
            tail = newSong;
            newSong.next = newSong;
            current = newSong;
        } else {
            newSong.next = head;
            tail.next = newSong;
            tail = newSong;
        }
    }

    public void removeSong(String title) {
        if (head == null) return;

        Song temp = head;
        Song prev = tail;

        do {
            if (temp.title.equalsIgnoreCase(title)) {
                if (temp == head) {
                    head = head.next;
                    tail.next = head;
                } else {
                    prev.next = temp.next;
                    if (temp == tail) {
                        tail = prev;
                    }
                }

                if (current == temp) {
                    current = temp.next;
                }

                // If only one song
                if (head == temp && head.next == temp) {
                    head = null;
                    tail = null;
                    current = null;
                }

                return;
            }
            prev = temp;
            temp = temp.next;
        } while (temp != head);
    }

    public void playNext() {
        if (current != null) {
            current = current.next;
        }
    }

    public void playPrevious() {
        if (current == null || current == current.next) return;

        Song temp = current;
        do {
            if (temp.next == current) {
                current = temp;
                break;
            }
            temp = temp.next;
        } while (temp != current);
    }

    public Song getCurrentSong() {
        return current;
    }

    public void shuffle() {
        if (head == null || head.next == head) return;

        List<Song> songs = new ArrayList<>();
        Song temp = head;
        do {
            songs.add(temp);
            temp = temp.next;
        } while (temp != head);

        Collections.shuffle(songs);
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).next = songs.get((i + 1) % songs.size());
        }

        head = songs.get(0);
        tail = songs.get(songs.size() - 1);
        tail.next = head;
        current = head;
    }

    public String getTotalDuration() {
        int total = 0;
        Song temp = head;
        if (temp == null) return "0:00";

        do {
            total += temp.duration;
            temp = temp.next;
        } while (temp != head);

        int minutes = total / 60;
        int seconds = total % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void displayPlaylist() {
        if (head == null) {
            System.out.println("Playlist kosong.");
            return;
        }

        System.out.println("=== Current Playlist ===");

        Song temp = head;
        int index = 1;
        do {
            String currentMark = (temp == current) ? "-> Currently Playing: " : "   " + index + ". ";
            System.out.println(currentMark + temp.title + " - " + temp.artist + " (" + temp.formatDuration() + ")");
            temp = temp.next;
            index++;
        } while (temp != head);

        System.out.println("Total Duration: " + getTotalDuration());
    }
}

// ========================
// Class Main (Contoh Penggunaan)
// ========================
public class Main {
    public static void main(String[] args) {
        CircularPlaylist playlist = new CircularPlaylist();

        playlist.addSong("Bohemian Rhapsody", "Queen", 363);
        playlist.addSong("Hotel California", "Eagles", 391);
        playlist.addSong("Stairway to Heaven", "Led Zeppelin", 482);
        playlist.addSong("Imagine", "John Lennon", 183);

        playlist.displayPlaylist();

        System.out.println("\nPlay next:");
        playlist.playNext();
        playlist.displayPlaylist();

        System.out.println("\nShuffle:");
        playlist.shuffle();
        playlist.displayPlaylist();
    }
}
