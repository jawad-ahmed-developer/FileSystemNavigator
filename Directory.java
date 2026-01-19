import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Directory {

    // Directory Class
    public static class Dir {
        String dir_name;
        List<Dir> sub_dirs;
        List<String> files;

        Dir(String dir_name) {
            this.dir_name = dir_name;
            sub_dirs = new ArrayList<>();
            files = new ArrayList<>() ;
        }
    }

    private final Dir home_dir = new Dir("Home"); // Root Directory
    private String current_path = home_dir.dir_name ;
    private String copyCutFile = null ;
    private Dir copyCutFolder = null ;
    private Dir copy_or_cut = null ;


    Dir getCopyFolder(){
        return copyCutFolder ;
    }

    void setCopyFolder(Dir copyFolder){
        this.copyCutFolder = copyFolder ;
    }

    void setCopyFile(String copyFile){
        this.copyCutFile = copyFile ;
    }

    String getCopyFile(){
        return copyCutFile ;
    }

    Dir getHome_dir() {
        return home_dir;
    }

    String getCurrent_path(){
        return current_path ;
    }

    Dir destinationDirectory(String[] path) throws Exception {

        Dir current = home_dir;

        for (int i = 1; i < path.length; i++) {
            boolean found = false;

            for (Dir sub : current.sub_dirs) {
                if (sub.dir_name.equalsIgnoreCase(path[i])) {
                    current = sub;
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new Exception("Path mismatch: " + path[i]);
            }
        }

        return current ;
    }

    void addDir(String dir_name) throws Exception{
        if (dir_name == null || dir_name.isBlank()) {
            throw new Exception("Directory name is invalid");
        }
        if(dir_name.contains("/")){
            throw new Exception("/ not allowed") ;
        }

        String[] path = current_path.split("/");

        Dir current = null ;
        try {
             current = destinationDirectory(path);
        }catch (Exception e){
           throw e ;
        }

        for (Dir sub : current.sub_dirs) {
            if (sub.dir_name.equalsIgnoreCase(dir_name)) {
                throw new Exception("Duplicate directory name: " + dir_name);
            }
        }

        current.sub_dirs.add(new Dir(dir_name));
        System.out.println("Directory added successfully") ;
    }

    void addFile(String file_name) throws Exception{
        if(file_name == null || file_name.isBlank()){
            throw new Exception("Invalid file name") ;
        }

        if(file_name.contains("/")){
            throw new Exception("/ not allowed") ;
        }

        String[] path = current_path.split("/");

       Dir current = null ;
        try{
            current = destinationDirectory(path) ;
        }catch (Exception e){
            throw e ;
        }

        for (String files : current.files) {
            if (files.equalsIgnoreCase(file_name)) {
                throw new Exception("Duplicate file name: " + file_name);
            }
        }

        current.files.add(file_name);
        System.out.println("File added successfully") ;
    }

    void traverseForward(String dir_name)throws Exception{
        if(dir_name == null || dir_name.isBlank()){
            throw new Exception("Invalid directory name") ;
        }

        String[] path = current_path.split("/") ;

        Dir current = null ;

        try{
            current =  destinationDirectory(path) ;
        }catch(Exception e){
            throw e;
        }

        boolean found = false ;
        for(Dir d:current.sub_dirs){
            if(d.dir_name.equalsIgnoreCase(dir_name)){
                found = true ;
                break ;
            }
        }

        if(found){
            current_path += "/" + dir_name.trim() ;
            return ;
        }
        throw new Exception("Directory "+ dir_name+" not found") ;
    }

    void back() throws Exception{
        if(current_path.equalsIgnoreCase("Home")){
              throw new Exception("Currently At Home directory. Can not go back!") ;
        }
        current_path = current_path.substring(0, current_path.lastIndexOf('/'));
    }

    void rename(String prev_name,String new_name,String file_or_directory) throws Exception{
        if(prev_name == null || prev_name.isBlank()){
            throw new Exception("Invalid previous name") ;
        }
        if(new_name == null || new_name.isBlank()){
            throw new Exception("Invalid new name") ;
        }

        if(file_or_directory == null || file_or_directory.isBlank()){
            throw new Exception("Choose what to rename file or directory") ;
        }

        if(!file_or_directory.equalsIgnoreCase("Dir") &&
                !file_or_directory.equalsIgnoreCase("File")){
            throw new Exception("Insert either File or Dir") ;
        }

        if(prev_name.equalsIgnoreCase("Home")){
            throw new Exception("Can not rename Home directory") ;
        }

        String[] path = current_path.split("/") ;

        Dir current = null ;
        try{
            current = destinationDirectory(path) ;
        }catch (Exception e){
           throw e ;
        }

        int found = -1 ;
        if(file_or_directory.equalsIgnoreCase("Dir")) {

            for (int i = 0 ; i< current.sub_dirs.size() ; i++) {
                if (current.sub_dirs.get(i).dir_name.equalsIgnoreCase(prev_name)) {
                    found = i;
                    break ;
                }
            }

           if(found == -1){
               throw new Exception("Directory not found to rename") ;
           }

           int count = 0 ;
           for(Dir d: current.sub_dirs){
               if(d.dir_name.equalsIgnoreCase(new_name)){
                   count++ ;
                   break ;
               }
           }

           if(count>0){
               throw new Exception("Duplicate Directory name") ;
           }

           current.sub_dirs.get(found).dir_name = new_name ;

        }else{

            for(int i = 0 ; i<current.files.size() ; i++){
                if(current.files.get(i).equalsIgnoreCase(prev_name)){
                    found = i ;
                    break ;
                }
            }

            if(found == -1){
                throw new Exception("File not found to rename") ;
            }

            int count = 0 ;
            for(String s:current.files){
                if(s.equalsIgnoreCase(new_name)){
                    count++ ;
                    break;
                }
            }

            if(count>0){
                throw new Exception("Duplicate file name") ;
            }

            current.files.set(found,new_name);

        }
    }

    void copyFile(String file_name) throws Exception{

        if(file_name == null || file_name.isBlank()){
            throw new Exception("Null or Empty file name") ;
        }

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        }catch (Exception e){
            throw e ;
        }

        boolean found = false ;
        for(String s:current.files){
            if(s.equalsIgnoreCase(file_name)){
                found = true ;
                break ;
            }
        }

        if(!found){
            throw new Exception("File " + file_name + " Does not exists") ;
        }

        if(copyCutFolder != null){
            copyCutFolder = null ;
        }
        if(copy_or_cut != null) {
            copy_or_cut.files.add(copyCutFile) ;
            copy_or_cut = null ;
        }
        copyCutFile = file_name ;

    }

    void copyFolder(String folder_name) throws Exception{

        if(folder_name == null || folder_name.isBlank()){
            throw new Exception("Invalid Folder name") ;
        }

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        }catch (Exception e){
            throw e ;
        }

        Dir found = null ;
        for(Dir d:current.sub_dirs){
            if(d.dir_name.equalsIgnoreCase(folder_name)){
                found = d ;
                break ;
            }
        }

        if(found == null){
            throw new Exception("Directory not found") ;
        }

        if(copyCutFile != null){
            copyCutFolder = null ;
        }
        if(copy_or_cut != null){
            copy_or_cut.sub_dirs.add(copyCutFolder) ;
            copy_or_cut = null ;
        }

        copyCutFolder = found ;

    }

    void cutFile(String file_name)throws Exception{
        if(file_name == null || file_name.isBlank()){
            throw new Exception("Invalid File");
        }

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        }catch (Exception e){
            throw e ;
        }

        String file = null ;
        for(int i = 0 ; i < current.files.size(); i++){
            if(current.files.get(i).equalsIgnoreCase(file_name)){
                file = current.files.remove(i) ;
                break ;
            }
        }

        if(file == null){
            throw new Exception("File not Found to cut") ;
        }

        if(copyCutFolder != null){
            copyCutFolder = null ;
        }

        if(copy_or_cut != null){
            copy_or_cut.files.add(copyCutFile) ;
        }
        copy_or_cut = current ;
        copyCutFile = file ;

    }

    void cutFolder(String dir_name) throws Exception{
        if(dir_name == null || dir_name.isBlank()){
            throw new Exception("Invalid Directory name") ;
        }

        Dir current = null ;
        try {
            current = destinationDirectory(current_path.split("/")) ;
        }catch(Exception e){
            throw e ;
        }

        Dir dir = null ;
        for(int i =0 ; i<current.sub_dirs.size() ; i++){
            if(current.sub_dirs.get(i).dir_name.equalsIgnoreCase(dir_name)){
                dir = current.sub_dirs.remove(i) ;
                break ;
            }
        }

        if(dir == null){
            throw new Exception("Directory to be cut not found") ;
        }

        if(copyCutFile != null){
            copyCutFile = null ;
        }

        if(copy_or_cut != null){
            copy_or_cut.sub_dirs.add(copyCutFolder) ;
        }
        copy_or_cut = current ;
        copyCutFolder = dir ;

    }

    void paste() throws Exception{

        if(copyCutFolder == null && copyCutFile == null){
           return ;
        }

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        } catch (Exception e) {
            throw e ;
        }

        if(copyCutFile == null){

            for(Dir d:current.sub_dirs){
                if(d.dir_name.equalsIgnoreCase(copyCutFolder.dir_name)){
                    throw new Exception("Duplicate name Found. Failed to Paste " + copyCutFolder.dir_name + " folder") ;
                }
            }

            current.sub_dirs.add(copyCutFolder) ;
            if(copy_or_cut != null){
                copy_or_cut = null ;
                copyCutFolder = null ;
            }

        }else{

            for(String f:current.files){
                if(f.equalsIgnoreCase(copyCutFile)){
                  throw new Exception("Duplicate name Found. Failed to paste " + f + " file") ;
                }
            }

            current.files.add(copyCutFile) ;
            if(copy_or_cut != null){
                copy_or_cut = null ;
                copyCutFile = null ;
            }
        }

    }

    void listFiles()throws Exception{

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        }catch (Exception e){
            throw e ;
        }

        if(current.files.isEmpty()){
            throw new Exception("No files found") ;
        }
        System.out.println("Current Location: " + current_path);
        System.out.println();
        for(String s:current.files){
            System.out.println(s) ;
        }

    }

    void listFolders()throws Exception{

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        } catch (Exception e) {
            throw e;
        }

        if(current.sub_dirs.isEmpty()){
            throw new Exception("No folder found") ;
        }

        System.out.println("Current Location: " + current_path);
        for(Dir d:current.sub_dirs){
            System.out.println(d.dir_name) ;
        }

    }

    void deleteFile(String file) throws Exception{

        if(file == null || file.isBlank()){
            throw new Exception("Invalid file name") ;
        }

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        }catch (Exception e){
            throw  e ;
        }
        boolean found = false ;
        for(int i = 0 ; i<current.files.size() ; i++){
            if(current.files.get(i).equalsIgnoreCase(file)){
                current.files.remove(i) ;
                found = true ;
                break ;
            }
        }

        if(!found){
            throw new Exception("File not found to delete") ;
        }

        if(copyCutFile != null && copyCutFile.equalsIgnoreCase(file)){
            copyCutFile = null ;
        }

    }

    void deleteFolder(String dir_name) throws Exception{

        if(dir_name == null || dir_name.isBlank()){
            throw new Exception("Invalid Directory name") ;
        }

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        }catch (Exception e){
            throw e ;
        }

        boolean found = false ;
        for(int i = 0 ; i<current.sub_dirs.size() ; i++){
            if(current.sub_dirs.get(i).dir_name.equalsIgnoreCase(dir_name)){
                current.sub_dirs.remove(i) ;
                found = true ;
                break ;
            }
        }

        if(!found){
            throw new Exception("Directory to delete not found") ;
        }

        if(copyCutFolder != null && copyCutFolder.dir_name.equalsIgnoreCase(dir_name)){
            copyCutFolder = null ;
            copy_or_cut = null ;
        }

    }

    void sort(String asc_desc_order) throws Exception{
        if(asc_desc_order == null || (!asc_desc_order.equalsIgnoreCase("asc") &&
                !asc_desc_order.equalsIgnoreCase("desc"))){
            throw new Exception("Invalid sort type") ;
        }

        Dir current = null ;
        try{
            current = destinationDirectory(current_path.split("/")) ;
        }catch (Exception e){
            throw  e;
        }

       if(!current.files.isEmpty() || !current.sub_dirs.isEmpty()){

           if(asc_desc_order.equals("asc")){

               current.files.sort(Comparator.naturalOrder());
               for(int i = 1 ; i<current.sub_dirs.size() ; i++){
                   Dir key = current.sub_dirs.get(i);
                   int j = i - 1 ;
                   while(j>=0 && current.sub_dirs.get(j).dir_name.charAt(0) > key.dir_name.toLowerCase().charAt(0)){
                        current.sub_dirs.set(j+1,current.sub_dirs.get(j)) ;
                        j-- ;
                   }
                   current.sub_dirs.set(j + 1, key);
               }

           }else{

               current.files.sort(Comparator.reverseOrder());
               for(int i = 1 ; i<current.sub_dirs.size() ; i++){
                   Dir key = current.sub_dirs.get(i);
                   int j = i - 1 ;
                   while(j>=0 && current.sub_dirs.get(j).dir_name.charAt(0) < key.dir_name.toLowerCase().charAt(0)){
                       current.sub_dirs.set(j+1,current.sub_dirs.get(j)) ;
                       j-- ;
                   }
                   current.sub_dirs.set(j + 1, key);
               }

           }
       }

    }

    void direct_jump(String path) throws Exception{

        if(path == null || path.isBlank()){
            throw new Exception("Invalid path to jump") ;
        }

        Dir current = null ;
        try{
            current = destinationDirectory(path.split("/")) ;
        }catch(Exception e){
            throw e ;
        }

        current_path = path ;

    }

    String search(String file_or_folder,Dir current) throws Exception{
        if(file_or_folder == null || file_or_folder.isBlank()){
            throw new Exception("Invalid file/folder name");
        }

        if(current.files.isEmpty() && current.sub_dirs.isEmpty()){
            return null ;
        }

        for(String s:current.files){
            if(s.equalsIgnoreCase(file_or_folder)){
               return current.dir_name ;
            }
        }

        for(Dir d:current.sub_dirs){
            if(d.dir_name.equalsIgnoreCase(file_or_folder)){
                return d.dir_name ;
            }
        }

        for(Dir d:current.sub_dirs){
            String path = search(file_or_folder,d) ;
            if(path != null) {
                return current.dir_name + "/" + path;
            }
        }

        return null ;
       }

}
