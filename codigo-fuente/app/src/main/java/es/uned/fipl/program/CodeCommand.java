package es.uned.fipl.program;

/**
 * Created by Gemma Lara Savill on 07/03/2015.
 */
public class CodeCommand {

    private int id; // para referenciar los sensores
    private String commandParameter;
    private String commandName;

    public CodeCommand(int id, String name, String param) {
        this.id = id;
        this.commandName = name;
        this.commandParameter = param;
   }

    public String getCommandParameter() {
        return commandParameter;
    }

    public void setCommandParameter(String commandParameter) {
        this.commandParameter = commandParameter;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getJavascript() {
        return "Command in javascript";
    }

    public String getJava() {
        return "Command in Java";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void printOut() {
        System.out.println("ID: "+id+" Comando: " + commandName + " Parámetro: " + commandParameter);
    }


}
