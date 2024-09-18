import { OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2'; // Importamos Swal para crear alertas visuales con Sweetalert2
import { CommonService } from '../services/common.service';
import { Generic } from '../models/generic';


// Marcamos esta clase como abstracta para que no pueda ser instanciada directamente. Sólo puede ser instanciada por aquellas clases que la heredan
export abstract class CommonFormComponent<E extends Generic,S extends CommonService<E>> implements OnInit {
  // Si no se indica un modificador de visibilidad en las propiedades de una clase, por defecto son públicas
  // Las propiedades de esta clase, que se quieran usar en la vista(html) asociada a este componente, tienen que ser públicas
  // Las propiedades privadas sólo se pueden usar dentro de esta clase y no tienen visibilidad en la vista(html)
  // Las propiedades protected se pueden usar en la clase padre y en aquellas clases hijas que hereden esa clase padre

  titulo: string;
  entidad: E;
  error: any;
  protected redirect: string;
  protected nombreModelo: string;

  // El constructor se debe usar para inyectar servicios y para realizar inicializaciones simples de propiedades
  constructor( // Este servicio, este router y este route se pasarán desde las clases hijas que hereden esta clase
    protected service: S,
    protected router: Router,
    protected route: ActivatedRoute
    ) { 

  }

  // Este método del ciclo de vida de un componente se debe usar para realizar inicializaciones complejas como, por ejemplo, realizar invocaciones a métodos de servicios que realizan peticiones http a backends
  ngOnInit(): void {
    this.titulo = `Crear ${this.nombreModelo}`;
    // Obtenemos un Observable con los parámetros que viajan en la url y nos suscribimos a este Observable para recuperar el parámetro "id" que se corresponde con el id de la entidad
    this.route.paramMap.subscribe(params => {
      // Como el método "get" del objeto "params" devuelve el valor de parámetro "id" como una cadena de texto o String, usamos '+' para convertirlo a un tipo de dato numérico
      const id: number = +params.get("id");
      // Como este componente está asociado tanto a la ruta para crear una nueva entidad como a la ruta para editar una entidad existente a través de su id, si el parámetro "id" recuperado en el paso anterior existe, significa que estamos en una edición de una entidad y hacemos lo siguiente:
      if(id){
        // Invocamos al método "ver" de nuestro servicio "service", pasándole el id de la entidad recuperado de la url, para obtener de nuestro backend API REST los datos de la entidad a partir de ese id
        // Como este método nos devuelve un Observable de tipo E con los datos de la entidad localizada a partir de su id, nos suscribimos a este Observable para porder pasar esos datos a la propiedad "entidad" de esta clase y así poder mostrarlos en el formulario de la vista(html) asociada a este componente
        this.service.ver(id).subscribe(entidad => this.entidad = entidad);
        this.titulo = `Editar ${this.nombreModelo}`;
      }
    });
  }

  // Método para crear una nueva entidad en nuestro backend API REST a partir de los datos de la entidad, introducidos por el usuario en el formulario, contenidos en la propiedad "entidad" de esta clase
  // Este método está asociado al evento OnClick del botón del formulario que se encuentra en la vista(html) asociada a este componente
  crear(): void{
    // Invocamos al método "crear" de nuestro servicio "service", pasándole los datos de la entidad contenidos en la propiedad "entidad" de esta clase, para crear una nueva entidad en nuestro backend API REST
    // Como este método nos devuelve un Observable de tipo E con los datos de la entidad creada, nos suscribimos a este Observable para poder mostrar esos datos por consola, mostrar también una alerta visual al usuario y navegar a la ruta establecida en la propiedad "redirect" de esta clase
    this.service.crear(this.entidad).subscribe(entidad => {
      console.log(entidad);
      // Mostramos al usuario una alerta visual de Sweetalert2 con el título "Creado:", un mensaje de éxito y un icono de éxito
      Swal.fire('Creado:',`${this.nombreModelo} ${entidad.nombre} creado con éxito`,'success');
      this.router.navigate([this.redirect]);
    },
    // Si el método "crear" nos devuelve un Observable con algún error, significa que nuestro backend API REST ha devuelto ese error en relación con la creación de la nueva entidad
    err => {
      // Si el código de ese error es 400(Bad Request), se trata de un error de validación de datos y significa que los datos de la nueva entidad a crear enviados a nuestro backend API REST no son correctos
      // En este caso, pasamos el contenido del error, con los mensajes de error de validación de datos, a la propiedad "error" de esta clase para poder mostrar esos mensajes de error en el formulario de la vista(html) asociada a este componente y también mostramos el contenido de ese error por consola
      if(err.status === 400)
        this.error = err.error;
        console.log(this.error);
    });
  }

  // Método para editar una entidad existente en nuestro backend API REST a partir de los nuevos datos de la entidad, introducidos por el usuario en el formulario, contenidos en la propiedad "entidad" de esta clase y también a partir del id de esa entidad
  // Este método está asociado al evento OnClick del botón del formulario que se encuentra en la vista(html) asociada a este componente
  editar(): void{
    // Invocamos al método "editar" de nuestro servicio "service", pasándole los nuevos datos de la entidad contenidos en la propiedad "entidad" de esta clase y el id de esa entidad, para actualizarlo en nuestro backend API REST
    // Como este método nos devuelve un Observable de tipo E con los datos de la entidad editada, nos suscribimos a este Observable para poder mostrar esos datos por consola, mostrar también una alerta visual al usuario y navegar a la ruta establecida en la propiedad "redirect" de esta clase
    this.service.editar(this.entidad,this.entidad.id).subscribe(entidad => {
      console.log(entidad);
      // Mostramos al usuario una alerta visual de Sweetalert2 con el título "Editado:", un mensaje de éxito y un icono de éxito
      Swal.fire('Editado:',`${this.nombreModelo} ${entidad.nombre} actualizado con éxito`,'success');
      this.router.navigate([this.redirect]);
    },
    // Si el método "editar" nos devuelve un Observable con algún error, significa que nuestro backend API REST ha devuelto ese error en relación con la edición de la entidad
    err => {
      // Si el código de ese error es 400(Bad Request), se trata de un error de validación de datos y significa que los nuevos datos de la entidad a actualizar enviados a nuestro backend API REST no son correctos
      // En este caso, pasamos el contenido del error, con los mensajes de error de validación de datos, a la propiedad "error" de esta clase para poder mostrar esos mensajes de error en el formulario de la vista(html) asociada a este componente y también mostramos el contenido de ese error por consola
      if(err.status === 400)
        this.error = err.error;
        console.log(this.error);
    });
  }

}
