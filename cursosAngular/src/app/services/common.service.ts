import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Generic } from '../models/generic';

// Servicio genérico para todas las entidades o modelos de nuestra aplicación
// "E" representa una entidad genérica de nuestro modelo(Puede ser Alumno, Curso, Exámen, etc...)

export abstract class CommonService<E extends Generic> { // Marcamos esta clase como abstracta para que no pueda ser instanciada directamente. Sólo puede ser instanciada por aquellas clases que la heredan
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  protected baseEndPoint: string; // url del Gateway de nuestro backend API REST donde se encuentra cada microservicio
  protected headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' }); // cabecera para indicar el tipo de contenido que envíamos a nuestro backend API REST mediante peticiones http de tipo Post y Put. Concretamente, estamos indicando que los datos que enviamos a nuestro backend API REST van en formato json

  constructor(protected httpClient: HttpClient) { // Este HttpClient se pasará desde las clases hijas que hereden esta clase

  }

  // Los Observable son asíncronos, no bloqueantes e inmutables. Inmutable quiere decir que cuando se aplican operadores, como por ejemplo "map", a un Observable, se crea un nuevo Observable con el resultado de aplicar el operador sin modificar el Observable original

  public listar(): Observable<E[]>{
    // Realiza una petición http Get a nuestro backend API REST a la url o ruta indicada por la propiedad "baseEndPoint" de esta clase
    // El método "get" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo E[]
    // El método "pipe" nos permite aplicar operadores como "map" a un flujo Observable
    return this.httpClient.get(this.baseEndPoint).pipe( // En lugar de usar el método "pipe" con el operador "map" para transformar el flujo Observable genérico de tipo "any", devuelto por el método "get", a un flujo Observable de tipo E[], podemos usar directamente la expresión "<E[]>" sobre el método "get"
      map(respuesta => respuesta as E[]) // El operador "as" realiza un casting para convertir, en este caso, el Observable genérico de tipo "any" contenido en "respuesta" en un Observable de tipo E[]
    );
  }

  public listarPaginas(page: string,size: string): Observable<any>{
    // Creamos un objeto de tipo HttpParams con el número de página y el tamaño de página contenidos en los argumentos de entrada "page" y "size" respectivamente para que viajen en la petición http como parámetros de la url
    const params = new HttpParams() // Este constructor crea una instancia inmutable y, por esta razón, tenemos que establecer los parámetros de forma encadenada para que no se creen nuevas instancias de tipo HttpParams cada vez que invoquemos al método "set" de manera independiente
    .set('page', page)
    .set('size', size);

    // Realiza una petición http Get a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con el texto "/pagina" pasándole un objeto de opciones con los parámetros que van a viajar junto con la url de esta petición http obtenidos del objeto "params" creado en el paso anterior
    // En esta caso, como nuestro backend API REST devuelve un objeto de tipo Page con la lista de entidades paginada y no devuelve un objeto de tipo E[], como el tipo Page no es un tipo especifico de TypeScript y no tenemos ninguna clase modelo para este tipo de dato, devolvemos directamente el Observable genérico de tipo "any" devuelto por el método "get"
    return this.httpClient.get(this.baseEndPoint + "/pagina", { params: params });
  }

  public ver(id: number): Observable<E>{
     // Realiza una petición http Get a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con "/" y el valor del parámetro den entrada "id"
     // El método "get" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo E
     return this.httpClient.get<E>(`${this.baseEndPoint}/${id}`); // En este caso, en lugar de usar el método "pipe" junto con el operador "map" para convertir el Observable de tipo genérico "any", devuelto por el método "get", a un Observable de tipo E, dicha conversión la hacemos directamente usando la expresión "<E>" sobre el método "get"
  }

  public crear(entidad: E): Observable<E>{
     // Realiza una petición http Post a nuestro backend API REST a la url o ruta indicada por la propiedad "baseEndPoint" de esta clase pasándole el objeto de la entidad a crear en este backend y un objeto de opciones con la cabecera que indica que el tipo de contenido que vamos a enviar va en formato json
     // El método "post" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo E
     // El método "pipe" nos permite aplicar operadores como "map" a un flujo Observable
     return this.httpClient.post(this.baseEndPoint,entidad,{ headers: this.headers }).pipe( // En lugar de usar el método "pipe" con el operador "map" para transformar el flujo Observable genérico de tipo "any", devuelto por el método "post", a un flujo Observable de tipo E, podemos usar directamente la expresión "<E>" sobre el método "post"
      map(respuesta => respuesta as E) // El operador "as" realiza un casting para convertir, en este caso, el Observable genérico de tipo "any" contenido en "respuesta" en un Observable de tipo E
    );
  }

  public editar(entidad: E,id: number): Observable<E>{
    // Realiza una petición http Put a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con "/" y el valor del parámetro de entrada "id" pasándole el objeto de la entidad con los datos a actualizar en este backend y un objeto de opciones con la cabecera que indica que el tipo de contenido que vamos a enviar va en formato json
    // El método "put" devuelve un Observable genérico de tipo "any" que es necesario convertirlo a nuestro tipo deseado, es decir, a un Observable de tipo E
    return this.httpClient.put<E>(`${this.baseEndPoint}/${id}`,entidad,{ headers: this.headers }); // En este caso, en lugar de usar el método "pipe" junto con el operador "map" para convertir el Observable de tipo genérico "any", devuelto por el método "put", a un Observable de tipo E, dicha conversión la hacemos directamente usando la expresión "<E>" sobre el método "put"
  }

  public eliminar(id: number): Observable<void>{
    // Realiza una petición http Delete a nuestro backend API REST a la url o ruta formada por el contenido de la propiedad "baseEndPoint" de esta clase junto con "/" y el valor del parámetro den entrada "id"
    // Devolvemos un Observable de tipo "void" porque nuestro backend API REST no devuelve nada para peticiones http Delete a esta url o ruta. Entonces, el Observable genérico de tipo "any" devuelto por el método "delete" lo convertirmos a un Observable de tipo "void" usando la expresión "<void>" sobre este método "delete"
    return this.httpClient.delete<void>(`${this.baseEndPoint}/${id}`);
  }
}
