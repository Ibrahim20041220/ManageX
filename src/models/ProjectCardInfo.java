package models ;

public class ProjectCardInfo {


    private  String projectTitle; 
    private  String status;  
    private  String createdAt ;
    private  String endDate ;  

    private  int completedTasks;
    private  int totalTasks;



    public ProjectCardInfo(String projectTitle, String status,String createdAt,String endDate,int completedTasks, int totalTasks) {
        this.projectTitle = projectTitle;
        this.status = status;
        this.createdAt = createdAt ;
        this.endDate = endDate ;
        this.completedTasks = completedTasks;
        this.totalTasks = totalTasks;
    }




}