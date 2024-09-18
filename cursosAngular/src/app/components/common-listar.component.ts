import { OnInit, ViewChild } from '@angular/core';
import Swal from 'sweetalert2'; // Importamos Swal para crear alertas visuales con Sweetalert2
import { PageEvent, MatPaginator } from '@angular/material/paginator';
import { CommonService } from '../services/common.service';
import { Generic } from '../models/generic';

// "E" representa una entidad genérica de nuestro modelo(Puede ser Alumno, Curso, Exámen, etc...)
// "S" representa un servicio genérico para nuestras entidades o modelos para realizar peticiones http a nuestro backend API REST

// Marcamos esta clase como abstracta para que no pueda ser instanciada directamente. Sólo puede ser instanciada por aquellas clases que la heredan
export abstract  class CommonListarComponent<E extends Generic,S extends CommonService<E>> implements OnInit {
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en esta clase y en aquellas otras clases que hereden este clase

  titulo: string;
  protected nombreModelo: string;
  lista: E[] = [];
  totalRegistros: number = 0; // Propiedad que indica el número total de registros del paginador
  paginaActual: number = 0; // Propiedad que indica la página actual del paginador
  totalPorPagina: number = 4; // Propiedad que indica el número por defecto de registros por página del paginador
  pageSizeOptions: number[] = [3,5,10,25,100]; // Propiedad que contiene un array con opciones de cambio del número de resgistros por página del paginador

  @ViewChild(MatPaginator) // Anotación que localiza el componente hijo "MatPaginator" de la vista(html) y lo inyecta en esta propiedad. Ésto lo hacemos para poder cambiar el texto del label o etiqueta, que indica los registros por página del paginador, de inglés a español
  paginator: MatPaginator;

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor(protected service: S) { // Este servicio se pasará desde las clases hijas que hereden esta clase

  }

  // Este método del ciclo de vida de un componente se debe usar para realizar inicializaciones complejas como, por ejemplo, realizar invocaciones a métodos de servicios que realizan peticiones http a backends
  ngOnInit(): void {
    // Invocamos al método "listarEntidadesPorPagina" de esta clase para obtener el listado de entidades paginado desde nuestro backend API REST usando el número de página 0(propiedad "paginaActual" de esta clase inicializada a 0) y el número de alumnos por página 4(propiedad "totalPorPagina" de esta clase inicializada a 4)
    this.listarEntidadesPorPagina();
  }

  // Método asociado al evento Page del paginador de la vista(html) asociada a este componente. De esta manera, cada vez que se aumente o se disminuya el número de página del paginador se invocará este método
  // Este método actualiza la página actual del paginador, contenida en la propiedad "paginaActual" de esta clase, y el número de registros por página, contenido en la propiedad "totalPorPagina" de esta clase, a partir de los datos del evento Page producido
  // Una vez actualizada la página actual y el número de registros por página del paginador a través de los datos del evento, se invoca al método "listarEntidadesPorPagina" para obtener el listado de entidades paginado desde nuestro backend API REST correspondiente a esas actualizaciones
  paginar(event: PageEvent): void{
    this.paginaActual = event.pageIndex;
    this.totalPorPagina = event.pageSize;
    this.listarEntidadesPorPagina();
  }

  // Método que obtiene el listado de entidades paginado desde nuestro backend API REST a partir de los valores de las propiedades "paginaActual" y "totalPorPagina" de esta clase
  private listarEntidadesPorPagina(){
    // Como el método "listarPaginas" recibe el número de la pagína actual, contenido en la propiedad "paginaActual", y el número de registros por página, contenido en la propiedad "totalPorPagina", como cadenas de texto o Strings, tenemos que convertir el tipo de dato Number de esas propiedades a String 
    const paginaActual = this.paginaActual.toString();
    const totalPorPagina = this.totalPorPagina.toString();
    // Invocamos al método "listarPaginas" de nuestro servicio "service" para obtener un listado de entidades paginado desde nuestro backend API REST a partir del número de la página actual y del número de registros por página que se le pasan a este método
    // Como este método nos devuelve un Observable de tipo genérico "any" con un objeto que contiene los datos del paginador, nos suscribimos a este Observable para hacer las siguientes cosas:
    this.service.listarPaginas(paginaActual,totalPorPagina).subscribe(paginador => {
      // Pasamos el listado de entidades del paginador, que se localiza en la propiedad "content" del objeto "paginador", a la propiedad "lista" de esta clase
      // El tipo de dato del objeto contenido en la propiedad "content" es de tipo genérico "any", así que tenemos que hacer un casting al tipo E[] para poder pasar el listado de entidades a la propiedad "lista" de esta clase
      this.lista = paginador.content as E[];
      // Pasamos el número total de registros del paginador, que se localiza en la propiedad "totalElements" del objeto "paginador", a la propiedad "totalRegistros" de esta clase
      // El tipo de dato del objeto contenido en la propiedad "totalElements" es de tipo genérico "any", así que tenemos que hacer un casting al tipo de dato Number para poder pasar el número total de registros a la propiedad "totalRegistros" de esta clase
      this.totalRegistros = paginador.totalElements as number;
      // Por último, cambiamos el texto de la etiqueta o label correspondiente a los registros por página del paginador de inglés a español
      this.paginator._intl.itemsPerPageLabel = "Registros por página:";
    });
  }

  // Método para eliminar una entidad existente en nuestro backend API REST a partir de los datos de esa entidad
  // Este método está asociado al evento OnClick del botón "Eliminar" de la tabla de entidades que se encuentra en la vista(html) asociada a este componente
  eliminar(entidad: E): void{
    // Creamos una alerta visual de Sweetalert2 usando su método "fire" de tipo aviso para que pregunte al usuario si realmente desea eliminar la entidad y lo confirme
    // Al método "fire" le pasamos un objeto Json con la configuración de nuestra alerta
    Swal.fire({
      title: 'Cuidado!', // Título de alerta
      text: `¿Seguro que deseas eliminar a ${entidad.nombre}?`, // Texto de la alerta
      icon: 'warning', // Indicamos el tipo de icono a mostrar en la alerta. En este caso, queremos un icono tipo aviso o warning
      showCancelButton: true, // Hacemos que muestre el botón de cancelación en la alerta visual
      confirmButtonColor: '#3085d6', // Modificamos el color por defecto del botón de confirmación por este
      cancelButtonColor: '#d33', // Modificamos el color por defecto del botón de cancelación por este
      confirmButtonText: 'Sí, eliminar!', // Modificamos el texto por defecto del botón de confirmación por este
      cancelButtonText: 'No, cancelar!' // Modificamos el texto por defecto del botón de cancelación por este
    }).then((result) => {
      // Si el usuario confirma la eliminación de la entidad haciendo click en el botón de confirmación de la alerta, se hace lo siguiente:
      if (result.value) {
        // Invocamos al método "eliminar" de nuestro servicio "service", pasándole el id de la entidad a eliminar, para eliminar esa entidad de nuestro backend API REST
        // Como este método nos devuelve un Observable de tipo "void"(nada), nos suscribimos a este Observable para poder invocar al método "listarEntidadesPorPagina" de esta clase para obtener el nuevo listado de entidades paginado desde nuestro backend API REST y para mostrar al usuario una alerta visual con un mensaje de éxito
        this.service.eliminar(entidad.id).subscribe(() => {
          this.listarEntidadesPorPagina();
          // Mostramos al usuario una alerta visual de Sweetalert2 con el título "Eliminado:", un mensaje de éxito y un icono de éxito
          Swal.fire('Eliminado:',`${this.nombreModelo} ${entidad.nombre} eliminado con éxito`,'success');
        });
      }
    });   
  }

}
