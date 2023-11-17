package Project;

import javax.swing.*;	
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Course_Management {
    public static void main(String[] args) throws IOException{

        //checking user type login
        do {
            LoginMenu login = new LoginMenu();
            login.userType(login.Login_menu());
        }while(JOptionPane.showConfirmDialog(null, "Do you want to end this system?", "Confirmation", JOptionPane.YES_NO_OPTION) == 1);
    }
}

class LoginMenu {
	
    public String enteredUsername,enteredPassword,name;
    int Login_menu() throws IOException {
        int loginInput = -1;
        //Login Menu
        do {
            try {
                loginInput = Integer.parseInt(JOptionPane.showInputDialog(null, """
        
        
                        --------Course Management System--------
        
                        1) Student Login
                        2) Lecturer Login
                        3) Admin Login
                        0) Exit
        
                        Enter Selection:\s
        
                        ""","Main Menu",JOptionPane.INFORMATION_MESSAGE));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid input.");
            }
            if(loginInput < 0 || loginInput > 3)
                JOptionPane.showMessageDialog(null, "Please enter a number between 0-3.");
        } while(!(loginInput >= 0 && loginInput < 4));

        //validate the username and password
        do
            if (loginInput == 0) System.exit(0);
        while (!validateLogin(loginInput));
        return loginInput;
    }

    boolean validateLogin(int loginInput) throws IOException {
        // Prompt the user for a username and password
        enteredUsername = JOptionPane.showInputDialog("Enter your username:");
        JPasswordField pf = new JPasswordField();

        if(JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            String pss = new String(pf.getPassword());
            try (BufferedReader reader = new BufferedReader(new FileReader(
                    "C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Acc\\" + loginInput + ".txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Split the line into a username and password using the delimiter ','
                    String[] parts = line.split(",");

                    String username = parts[0];
                    String password = parts[1];
                    if(loginInput == 1)
                        name = parts[2];

                    // Check if the entered username and password match this pair
                    if (enteredUsername.equals(username) && pss.equals(password)) {
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        return true;
                    }
                }
                // If we reach this point, the entered username and password were invalid
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        }return false;
    }

    void userType(int loginInput) throws IOException {
        //user determination using switch
        switch (loginInput) {
            case 1 -> {
                Student student = new Student(enteredUsername, enteredPassword,name);
                CourseRegistration course = new CourseRegistration();
                course.choosingSubj(student.getName());
            }
            case 2 -> {
                Lecturer lect = new Lecturer(enteredUsername, enteredPassword);
                lect.printCourse(enteredUsername);
            }
            case 3 -> {
                new Administrator(enteredUsername, enteredPassword);
                Course_administrator admin = new Course_administrator();
                String[] options = {"Add", "Delete"};
                int optionSelected = JOptionPane.showOptionDialog(null, "Choosing between add or delete course:",
                        "Course Management", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                //determine the admin choosing option between add or delete course
                if (optionSelected == 0) {admin.adding_course();}
                else if (optionSelected == 1) {admin.deleting_course();}
            }
        }
    }
}

class User {
    private final String username,password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Student extends User{
    private final String name;

    public Student(String username, String password, String name) {

        super(username, password);
        this.name = name;
    }

    //getter for getting the name of the student
    public String getName() {
        return name;
    }
}

class Lecturer extends User{
    public Lecturer(String username, String password) {
        super(username, password);
    }

    void printCourse(String name) throws IOException {

        String[] lectureName = {"Cik Rosni binti Ramle","En Abdul Halim bin Omar","En Fawwaz bin Mohd Nasir"};

        //read the file in the following path
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Lecture\\" + name + ".txt"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }br.close();
        //showing the content of the file
        JOptionPane.showMessageDialog(null,((name.equals("LA01"))? lectureName[0] : (name.equals("LA02"))?
                lectureName[1] : lectureName[2]) + ":\n\n" + sb, "Subject Hold", JOptionPane.INFORMATION_MESSAGE);
    }
}

class Administrator extends User{
    public Administrator(String username, String password) {
        super(username, password);
    }
}

class Course{

    //arraylist for subject chosen
    ArrayList<String> subjectChosen = new ArrayList<>();

    //arraylist for choosing the subject
    ArrayList<String> subjectName = new ArrayList<>();
    ArrayList<String> subjectNameTemp = new ArrayList<>();
    ArrayList<Integer> subjectSize = new ArrayList<>();
    ArrayList<Integer> subjectSizeTemp = new ArrayList<>();

    //getter for getting the course name
    private final String courseName = "Information Technology";
    public String getCourseName(){
        return courseName;
    }
}

class CourseRegistration extends Course{

    void choosingSubj(String name) throws IOException {

        String line;
        int subjectInp,i;

        //read the file path
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\SubjectName.txt"));
        while((line = br.readLine()) != null){
            //seperate the line into two for each name and size of the course into the certain arraylist
            String[] separate = line.split(",");
            subjectName.add(separate[0]);
            subjectSize.add(Integer.valueOf(separate[1]));

            subjectNameTemp.add(separate[0]);
            subjectSizeTemp.add(Integer.valueOf(separate[1]));
        }

        //printing the course list
        do {
            System.out.println("Code\tSubject Name \t\t    Size \t Credit Hour");
            for (i = 0; i < subjectName.size(); i++) {
                System.out.println((i + 1) + ".\t" + subjectName.get(i) + "   " + subjectSize.get(i) + "\t\t3");
            }
            //choosing the course
            subjectInp = Integer.parseInt(JOptionPane.showInputDialog(null, "Please choose the subject\n"));
            if (JOptionPane.showConfirmDialog(null, "Do you confirm?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {

                if(subjectInp > i){
                    continue;
                }else {
                    //adding the subject chosen into arraylist named subjectChosen and decrease the size of the course
                    subjectChosen.add(subjectName.get(subjectInp - 1));

                    for(i = 0; i < subjectNameTemp.size(); i++){
                        if(Objects.equals(subjectName.get(subjectInp-1),subjectNameTemp.get(i))){
                            Integer temp = subjectSizeTemp.get(subjectNameTemp.indexOf(subjectNameTemp.get(i)));
                            temp--;
                            subjectSizeTemp.set(subjectNameTemp.indexOf(subjectNameTemp.get(i)), temp);
                        }
                    }

                    //read the subject file path and remove the subject that have been chosen
                    BufferedWriter writer = new BufferedWriter(new FileWriter(
                            "C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\SubjectName.txt"));
                    for (i = 0; i < subjectSizeTemp.size(); i++) {
                        writer.write(subjectNameTemp.get(i) + "," + subjectSizeTemp.get(i) + "\n");
                    }
                    writer.close();
                    subjectName.remove(subjectInp - 1);
                    subjectSize.remove(subjectInp - 1);
                }
            }

            //clear the screen
            System.out.print("\033[H\033[2J");
            System.out.flush();

            //write the latest new course list from the temperorary file
            BufferedWriter writer = new BufferedWriter(new FileWriter(

                    "C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\SubjectNameTemp.txt"));
            for(i = 0; i < subjectNameTemp.size(); i++) {
                writer.write(subjectNameTemp.get(i) + "," + subjectSizeTemp.get(i) + "\n");
            }writer.close();

        } while (JOptionPane.showConfirmDialog(null, "Do you want to add more subject?", "Adding more subject",
                JOptionPane.YES_NO_OPTION) == 0);

        //print the slip
        Course_administrator admin = new Course_administrator();
        admin.printSlip(name,subjectChosen);

        //open the file by windows command
        File file = new File("C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\Subject_Slip.txt");
        Desktop.getDesktop().open(file);
    }
}

class Course_administrator extends Course{

    void printSlip(String name, ArrayList subjChose) throws FileNotFoundException {

        int count = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        //read the file path
        File file = new File("C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\Subject_Slip.txt");

        //write the content into the file
        PrintWriter out = new PrintWriter(file);

        out.println("------------------------------------------------\nDetail information:\n\nName : " + name.toUpperCase() +
                "\nCourse Name : " + getCourseName() + "\nSection : 3\nDate : " + dateFormat.format(date) + "\nTime : " + timeFormat.format(date) +
                "\n------------------------------------------------\n");
        out.println("No\tCourse\t\t \t     Credit Hour");
        for (Object s : subjChose) {
            out.println((count+1) + "\t" + s + "\t\t3");
            count++;
        }out.println("\n------------------------------------------------\nTotal Credit Hour: " + (count)*3 + "\n------------------------------------------------");

        //close the PrintWriter class
        out.close();
    }

    void adding_course() throws IOException {
        String sizeOfCourseAdd,courseAdd;
        boolean checkSubject;
        do {
			checkSubject = false;
            //input the new course name nad the size
            courseAdd = JOptionPane.showInputDialog(null, "Enter the new name subject: ");
            sizeOfCourseAdd = JOptionPane.showInputDialog(null, "Enter the size of the subject");

            FileReader fr = new FileReader("C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\SubjectName.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null) {
                if(line.contains(courseAdd.toUpperCase())) {
                    JOptionPane.showMessageDialog(null, "Subject already exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    checkSubject = true;
                    break;
                }
            }
            if (courseAdd.length() > 27 || sizeOfCourseAdd.length() > 2)
                JOptionPane.showMessageDialog(null, "Invalid course name or size");

        } while (courseAdd.length() > 27 || sizeOfCourseAdd.length() > 2 || checkSubject);

        //read the file to append the new course name
        FileWriter fileWriter = new FileWriter("C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\SubjectName.txt", true);

        //write the new course name
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(courseAdd.toUpperCase() + " ".repeat(Math.max(0, (27 - courseAdd.length()))) + "," + sizeOfCourseAdd);

        //close the bufferedWriter abd fileWriter class
        bufferedWriter.close();
        fileWriter.close();
    }

    void deleting_course() throws IOException {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\SubjectName.txt"));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println((i + 1) + ".\t " + line);
                i++;
            }reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //input the name of the existing course to delete
        String courseDel = JOptionPane.showInputDialog(null, "Enter the name of the subject: ","Delete Course", JOptionPane.ERROR_MESSAGE);

        //read the file path
        File file = new File("C:\\Users\\SCSM11\\Desktop\\Coding\\Java_OOP\\src\\Project\\Course\\SubjectName.txt");

        //make a temperarory file
        File tempFile = File.createTempFile("temp", ".txt");

        //read and write or update the content of the file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line;
        while ((line = reader.readLine()) != null) if (!line.contains(courseDel.toUpperCase())) {
            writer.write(line);
            writer.newLine();
        }

        //close the classes
        reader.close();
        writer.close();
        file.delete();
        tempFile.renameTo(file);
    }
}