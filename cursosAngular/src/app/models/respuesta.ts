import { Alumno } from './alumno';
import { Pregunta } from './pregunta';

/* Esta clase ha sido generado con el comando "ng g class models/respuesta --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

// Esta clase representa nuestro modelo Respuesta

export class Respuesta {
    id: string;
    texto: string;
    alumno: Alumno;
    pregunta: Pregunta;
}
