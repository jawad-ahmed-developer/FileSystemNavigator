import java.util.Scanner;

public class Main {

    Directory navigator = new Directory();
    Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        Main fn = new Main();
        int selectedOption;

        do {
            selectedOption = fn.mainMenu();

            if (selectedOption == 1) {
                fn.createFolder();
            }
            else if (selectedOption == 2) {
                fn.createFile();
            }
            else if (selectedOption == 3) {
                fn.listFiles();
            }
            else if (selectedOption == 4) {
                fn.listFolders();
            }
            else if (selectedOption == 5) {
                fn.forward();
            }
            else if (selectedOption == 6) {
                fn.back();
            }
            else if (selectedOption == 7) {
                fn.rename();
            }
            else if (selectedOption == 8) {
                fn.copyFile();
            }
            else if (selectedOption == 9) {
                fn.copyFolder();
            }
            else if (selectedOption == 10) {
                fn.cutFile();
            }
            else if (selectedOption == 11) {
                fn.cutFolder();
            }
            else if (selectedOption == 12) {
                fn.paste();
            }
            else if (selectedOption == 13) {
                fn.deleteFile();
            }
            else if (selectedOption == 14) {
                fn.deleteFolder();
            }
            else if (selectedOption == 15) {
                fn.sort();
            }
            else if (selectedOption == 16) {
                fn.search();
            }
            else if (selectedOption == 17) {
                fn.directJump();
            }

        } while (selectedOption != 18);

        System.out.println("Exiting File System Navigator...");
    }

    /* -------------------- Menu Operations -------------------- */

    void createFolder() {
        String name;
        do {
            System.out.print("Enter Folder name: ");
            name = input.nextLine();
            try {
                navigator.addDir(name);
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!discard("Do you want to discard add Folder?(Y/N): "));
    }

    void createFile() {
        String name;
        do {
            System.out.print("Enter File name: ");
            name = input.nextLine();
            try {
                navigator.addFile(name);
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!discard("Do you want to discard add File?(Y/N): "));
    }

    void listFiles() {
        try {
            navigator.listFiles();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void listFolders() {
        try {
            navigator.listFolders();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void forward() {
        String name;
        do {
            System.out.print("Enter Folder name: ");
            name = input.nextLine();
            try {
                navigator.traverseForward(name);
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!discard("Do you want to discard Forward?(Y/N): "));
    }

    void back() {
        try {
            navigator.back();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void rename() {
        while (true) {
            System.out.print("Enter current name: ");
            String oldName = input.nextLine();
            System.out.print("Enter new name: ");
            String newName = input.nextLine();

            int choice = file_or_folder();
            try {
                if (choice == 1)
                    navigator.rename(oldName, newName, "File");
                else if (choice == 2)
                    navigator.rename(oldName, newName, "Dir");
                else
                    return;
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void copyFile() {
        retrySingleInput("Enter File name: ", true);
    }

    void copyFolder() {
        retrySingleInput("Enter Folder name: ", false);
    }

    void cutFile() {
        retrySingleInput("Enter File name: ", true, true);
    }

    void cutFolder() {
        retrySingleInput("Enter Folder name: ", false, true);
    }

    void paste() {
        try {
            navigator.paste();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void deleteFile() {
        retryDelete(true);
    }

    void deleteFolder() {
        retryDelete(false);
    }

    void sort() {
        System.out.print("Sort Order (asc/desc): ");
        String type = input.nextLine();
        try {
            navigator.sort(type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void search() {
        System.out.print("Enter File/Folder name: ");
        String name = input.nextLine();
        try {
            String result = navigator.search(name, navigator.getHome_dir());
            System.out.println(result == null ? "Not Found" : "Found at: Home/" + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void directJump() {
        String path;
        do {
            System.out.print("Enter Full Path (Home/...): ");
            path = input.nextLine();
            try {
                navigator.direct_jump(path);
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!discard("Do you want to discard Jump?(Y/N): "));
    }

    boolean discard(String msg) {
        while (true) {
            System.out.print(msg);
            String yn = input.nextLine();
            if (yn.equalsIgnoreCase("Y")) return true;
            if (yn.equalsIgnoreCase("N")) return false;
            System.out.println("Invalid Input!");
        }
    }

    void retrySingleInput(String prompt, boolean file) {
        retrySingleInput(prompt, file, false);
    }

    void retrySingleInput(String prompt, boolean file, boolean cut) {
        String name;
        do {
            System.out.print(prompt);
            name = input.nextLine();
            try {
                if (file && cut) navigator.cutFile(name);
                else if (!file && cut) navigator.cutFolder(name);
                else if (file) navigator.copyFile(name);
                else navigator.copyFolder(name);
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!discard("Do you want to discard operation?(Y/N): "));
    }

    void retryDelete(boolean file) {
        String name;
        do {
            System.out.print("Enter name: ");
            name = input.nextLine();
            try {
                if (file) navigator.deleteFile(name);
                else navigator.deleteFolder(name);
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!discard("Do you want to discard Delete?(Y/N): "));
    }

    int file_or_folder() {
        System.out.println("\n1. File\n2. Folder\n3. Exit");
        System.out.print("Enter choice: ");
        try {
            int opt = input.nextInt();
            input.nextLine();
            return opt;
        } catch (Exception e) {
            input.nextLine();
            return -1;
        }
    }

    int mainMenu() {
        System.out.println("\nCurrent Location: " + navigator.getCurrent_path());
        System.out.println("1. Create Folder");
        System.out.println("2. Create File");
        System.out.println("3. List Files");
        System.out.println("4. List Folders");
        System.out.println("5. Forward");
        System.out.println("6. Back");
        System.out.println("7. Rename");
        System.out.println("8. Copy File");
        System.out.println("9. Copy Folder");
        System.out.println("10. Cut File");
        System.out.println("11. Cut Folder");
        System.out.println("12. Paste");
        System.out.println("13. Delete File");
        System.out.println("14. Delete Folder");
        System.out.println("15. Sort");
        System.out.println("16. Search");
        System.out.println("17. Direct Jump");
        System.out.println("18. Exit");
        System.out.print("Enter Option (1-18): ");

        try {
            int opt = input.nextInt();
            input.nextLine();
            return opt;
        } catch (Exception e) {
            input.nextLine();
            return -1;
        }
    }
}
