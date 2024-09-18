import { Examen } from './examen';

/* Esta clase ha sido generado con el comando "ng g class models/pregunta --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

// Esta clase representa nuestro modelo Pregunta

export class Pregunta {
    id: number;
    texto: string;
    examen: Examen;
}
