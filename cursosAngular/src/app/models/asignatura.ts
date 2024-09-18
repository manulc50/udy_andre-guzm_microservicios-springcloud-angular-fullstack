
/* Esta clase ha sido generado con el comando "ng g class models/asignatura --skipTests=true"
   La opción "--skipTests=true" no genera el fichero "spec.ts" de pruebas unitarias
*/

// Esta clase representa nuestro modelo Asignatura

export class Asignatura {
    id: number;
    nombre: string;
    padre: Asignatura;
    hijos: Asignatura[] = [];
}
