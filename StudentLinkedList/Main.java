import java.util.ArrayList;
import java.util.List;

// Struktur Data Mahasiswa
class Student {
    String nim;
    String name;
    double gpa;
    Student next;

    public Student(String nim, String name, double gpa) {
        this.nim = nim;
        this.name = name;
        this.gpa = gpa;
        this.next = null;
    }
}

// Class Single Linked List
class StudentLinkedList {
    Student head;

    // ================================
    // SOAL 1: FUNGSI DASAR LINKED LIST
    // ================================

    // Cek apakah list kosong
    public boolean isEmpty() {
        return head == null;
    }

    // Hitung jumlah node
    public int size() {
        int count = 0;
        Student current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    // Insert di awal list
    public void insertFirst(String nim, String name, double gpa) {
        Student newStudent = new Student(nim, name, gpa);
        newStudent.next = head;
        head = newStudent;
    }

    // Insert di akhir list
    public void insertLast(String nim, String name, double gpa) {
        Student newStudent = new Student(nim, name, gpa);
        if (isEmpty()) {
            head = newStudent;
        } else {
            Student current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newStudent;
        }
    }

    // Insert di posisi tertentu
    public void insertAt(int position, String nim, String name, double gpa) {
        if (position < 0 || position > size()) {
            System.out.println("Posisi tidak valid");
            return;
        }

        if (position == 0) {
            insertFirst(nim, name, gpa);
            return;
        }

        Student newStudent = new Student(nim, name, gpa);
        Student current = head;
        for (int i = 0; i < position - 1; i++) {
            current = current.next;
        }
        newStudent.next = current.next;
        current.next = newStudent;
    }

    // Hapus node berdasarkan NIM
    public void deleteByNim(String nim) {
        if (isEmpty()) {
            System.out.println("List kosong");
            return;
        }

        if (head.nim.equals(nim)) {
            head = head.next;
            return;
        }

        Student current = head;
        while (current.next != null && !current.next.nim.equals(nim)) {
            current = current.next;
        }

        if (current.next == null) {
            System.out.println("Mahasiswa dengan NIM " + nim + " tidak ditemukan");
        } else {
            current.next = current.next.next;
        }
    }

    // Cari node berdasarkan NIM
    public Student search(String nim) {
        Student current = head;
        while (current != null) {
            if (current.nim.equals(nim)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // Tampilkan seluruh data mahasiswa
    public void display() {
        System.out.println("=== Data Mahasiswa ===");
        Student current = head;
        while (current != null) {
            System.out.println("NIM: " + current.nim + ", Nama: " + current.name + ", IPK: " + current.gpa);
            current = current.next;
        }
        System.out.println("Total mahasiswa: " + size());
    }

    // ==========================================
    // SOAL 2: OPERASI LANJUTAN PADA LINKED LIST
    // ==========================================

    // Bubble sort linked list berdasarkan IPK (descending)
    public void sortByGPA() {
        if (head == null || head.next == null) return;

        boolean swapped;
        do {
            swapped = false;
            Student current = head;
            while (current.next != null) {
                if (current.gpa < current.next.gpa) {
                    // Tukar isi data antar node
                    String tmpNim = current.nim;
                    String tmpName = current.name;
                    double tmpGpa = current.gpa;

                    current.nim = current.next.nim;
                    current.name = current.next.name;
                    current.gpa = current.next.gpa;

                    current.next.nim = tmpNim;
                    current.next.name = tmpName;
                    current.next.gpa = tmpGpa;

                    swapped = true;
                }
                current = current.next;
            }
        } while (swapped);
    }

    // Membalik urutan linked list
    public void reverse() {
        Student prev = null;
        Student current = head;
        Student next = null;

        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }

        head = prev;
    }

    // Mengambil mahasiswa dengan IPK tertinggi
    public Student findHighestGPA() {
        if (head == null) return null;

        Student highest = head;
        Student current = head.next;

        while (current != null) {
            if (current.gpa > highest.gpa) {
                highest = current;
            }
            current = current.next;
        }

        return highest;
    }

    // Mengambil list mahasiswa dengan IPK di atas ambang
    public List<Student> getStudentsAboveGPA(double threshold) {
        List<Student> result = new ArrayList<>();
        Student current = head;

        while (current != null) {
            if (current.gpa > threshold) {
                result.add(current);
            }
            current = current.next;
        }

        return result;
    }

    // Menggabungkan dua list terurut berdasarkan IPK (descending)
    public StudentLinkedList mergeSortedList(StudentLinkedList otherList) {
        StudentLinkedList merged = new StudentLinkedList();
        Student dummy = new Student("", "", 0);
        Student tail = dummy;

        Student a = this.head;
        Student b = otherList.head;

        while (a != null && b != null) {
            if (a.gpa >= b.gpa) {
                tail.next = new Student(a.nim, a.name, a.gpa);
                a = a.next;
            } else {
                tail.next = new Student(b.nim, b.name, b.gpa);
                b = b.next;
            }
            tail = tail.next;
        }

        while (a != null) {
            tail.next = new Student(a.nim, a.name, a.gpa);
            a = a.next;
            tail = tail.next;
        }

        while (b != null) {
            tail.next = new Student(b.nim, b.name, b.gpa);
            b = b.next;
            tail = tail.next;
        }

        merged.head = dummy.next;
        return merged;
    }
}

// Main program
public class Main {
    public static void main(String[] args) {
        StudentLinkedList list1 = new StudentLinkedList();
        list1.insertLast("12345", "Andi Pratama", 3.75);
        list1.insertLast("12346", "Sari Dewi", 3.82);
        list1.insertLast("12347", "Budi Santoso", 3.65);

        System.out.println("== Sebelum Sort ==");
        list1.display();

        list1.sortByGPA();
        System.out.println("== Setelah Sort by GPA Descending ==");
        list1.display();

        System.out.println("== Reverse List ==");
        list1.reverse();
        list1.display();

        Student top = list1.findHighestGPA();
        System.out.println("Mahasiswa IPK tertinggi: " + top.name + ", GPA: " + top.gpa);

        System.out.println("Mahasiswa dengan IPK > 3.7:");
        for (Student s : list1.getStudentsAboveGPA(3.7)) {
            System.out.println(s.name + " (" + s.gpa + ")");
        }

        StudentLinkedList list2 = new StudentLinkedList();
        list2.insertLast("54321", "Lina Maharani", 3.90);
        list2.insertLast("54322", "Doni Putra", 3.70);

        list2.sortByGPA(); // Pastikan list2 sudah terurut
        StudentLinkedList merged = list1.mergeSortedList(list2);
        System.out.println("== Merged Sorted List ==");
        merged.display();
    }
}
