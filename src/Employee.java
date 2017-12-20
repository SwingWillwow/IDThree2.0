public class Employee {
    private int id;
    private char educationLevel;//学历 C 代表专科 B 代表本科 M 代表研究生
    private char sex;//性别 M 代表男, F 代表女
    private int englishLevel;//英语水平 4 代表 4级, 6 代表6级, 2 代表4级以下
    private int characterType;//性格特征 1,2,3 分别代表 a_1,a_2,a_3 三类
    private int postType;//岗位性质 1,2,3 分别代表 b_1,b_2,b_3 三类
    private char classType;//类别,Y 表示及格，N 表示不及格

    /*
        getters and setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(char educationLevel) {
        this.educationLevel = educationLevel;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(int englishLevel) {
        this.englishLevel = englishLevel;
    }

    public int getCharacterType() {
        return characterType;
    }

    public void setCharacterType(int characterType) {
        this.characterType = characterType;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public char getClassType() {
        return classType;
    }

    public void setClassType(char classType) {
        this.classType = classType;
    }
}
