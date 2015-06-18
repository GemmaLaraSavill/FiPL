/*
* Copyright 2015 Gemma Samantha Lara Savill
* FiPL My First Programming Language
* Proyecto de Fin de Grado en Ingeniería en Tecnologías de la Información
* en la Universidad Nacional de Educación a Distancia UNED España
*
* Final project for my Bachelor of Science in Information Technology Engineering
* At the Spanish National Distance University UNED
* Project manager Dr. Anselmo Peñas Padilla

* Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* http://ec.europa.eu/idabc/eupl
*
* Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and limitations under the Licence.
*/
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
