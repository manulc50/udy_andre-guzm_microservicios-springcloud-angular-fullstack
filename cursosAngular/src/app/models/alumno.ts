import { Generic } from './generic';

/* Esta clase ha sido generado con el comando "ng g class models/alumno --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

// Esta clase representa nuestro modelo Alumno

export class Alumno implements Generic{
    id: number;
    nombre: string;
    apellidos: string;
    email: string;
    createAt: string;
    fotoHashCode: number;
}
