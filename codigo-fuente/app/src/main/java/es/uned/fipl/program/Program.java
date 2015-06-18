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

import java.util.ArrayList;

import es.uned.fipl.block.CommandBlock;
import es.uned.fipl.block.CommandBlockName;

/**
 * Created by Gemma Lara Savill on 07/03/2015.
 */
public class Program {

    private BlockProgram blockProgram;
    private ArrayList<CodeCommand> commandList;
    private boolean tellme = false;

    /**
     * Create a program from a blockProgram = list of blocks
     * @param blockProgram
     */
    public Program(BlockProgram blockProgram) {

        this.blockProgram = blockProgram;
        commandList = new ArrayList<CodeCommand>();
        if(tellme) { System.out.println("Program: creo lista comandos desde lista bloques"); }

        // load program from blockList
        int numberOfCommands = blockProgram.getSize();
        if (tellme) {
            System.out.println("Program: tengo " + numberOfCommands + " bloques");
            System.out.println("inicio de paso de blockList a Program");
            System.out.println("------------");
        }
        CodeCommand repeatCommand = null;
        for (int i = 0; i < numberOfCommands; i++) {
            String parameter = null;
            CommandBlock block = blockProgram.get(i).getBlock();
            BlockLink blockLink = blockProgram.get(i);
            if (block.isSensor()) {
                // sensores -> caso especial, deben ser insertados detrás del comando al que afectan
                int targetId = blockProgram.get(i).getAnchor();
                // get id of anchor in this program
                for (int j = 0; j < i; j++) {
                    if (this.getCommandList().get(j).getId() == targetId) {
//                        System.out.println("if "+this.getCommandList().get(j).getId() +"=="+ targetId);
//                        System.out.println("Target del sensor es " + this.getCommandList().get(j).getId());
                        parameter = block.getParameter();
                        CodeCommand codeCommand = new CodeCommand(block.getId(), block.getType().toString(), parameter);
                        this.insertCommand(j + 1, codeCommand);
                        j = i;
                    }
                }

            } else if (block.getType() == CommandBlockName.REPEAT) {
                // inicio de un bucle repetir
                // necesitaré pasar el comando siguiente al final del bucle como parámetro
                // se añadirá cuando surja para no hacer más bucles
                repeatCommand = new CodeCommand(block.getId(), block.getType().toString(), "");
                if (tellme) {
                    repeatCommand.printOut();
                }
                this.addCommand(repeatCommand);
            } else {
                    // caso normal
                    if (block.getType() == CommandBlockName.POSITION) {
                        // caso especial de parámetros: posición tiene dos parámetros x e y
                        parameter = block.getParameter() + "," + block.getParameter2();
                    } else {
                        parameter = block.getParameter();
                    }
                    CodeCommand codeCommand = new CodeCommand(block.getId(), block.getType().toString(), parameter);
                    if (tellme) {
                        codeCommand.printOut();
                    }
                    // veo si no es fin de bucle
                    if(repeatCommand != null) {
                        if (blockLink.getRepeat() == BlockLink.REPEAT_LAST) {
                            // añado este comando como final de bucle
                            repeatCommand.setCommandParameter(i+"");
                            repeatCommand = null;

                        }
                    }
                    this.addCommand(codeCommand);

            }
        } // fin de bucle de comandos


        if (tellme) {
            this.printProgram();
            System.out.println("------------");
            System.out.println("fin de paso de blockList a Program");
        }

    }

        public Program() {
            commandList = new ArrayList<CodeCommand>();
        }


    public void setCommandList(ArrayList<CodeCommand> commandList) {
        this.commandList = commandList;
        if (tellme) {
            System.out.println("Pasado nuevo comandos");
            this.printProgram();
        }
    }


    private void insertCommand(int j, CodeCommand codeCommand) {
        commandList.add(j, codeCommand);
    }

    public void addCommand(CodeCommand command) {
           commandList.add(command);
}


    public ArrayList<CodeCommand> getCommandList() {
        return commandList;
    }

    /**
     * Imprime el programa en la consola de Java
     */
    public void printProgram() {
        System.out.println("## Inicio program:");
        for(int i=0; i<this.getCommandList().size(); i++) {
            System.out.print(i+" ");
            this.getCommandList().get(i).printOut();
        }
        System.out.println("## Fin program.");
    }
}
