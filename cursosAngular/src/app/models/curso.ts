import { Generic } from './generic';
import { Alumno } from './alumno';
import { Examen } from './examen';

/* Esta clase ha sido generado con el comando "ng g class models/curso --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

// Esta clase representa nuestro modelo Curso

export class Curso implements Generic{
    id: number;
    nombre: string;
    createAt: string;
    alumnos: Alumno[] = [];
    examenes: Examen[] = [];
}
