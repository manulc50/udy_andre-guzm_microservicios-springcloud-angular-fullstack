import { Pregunta } from './pregunta';
import { Asignatura } from './asignatura';
import { Generic } from './generic';

/* Esta clase ha sido generado con el comando "ng g class models/examen --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

// Esta clase representa nuestro modelo Examen

export class Examen implements Generic{
    id: number;
    nombre: string;
    createAt: string;
    preguntas: Pregunta[] = [];
    asignatura: Asignatura;
    respondido: boolean;
}
