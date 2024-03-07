package com.example.receapp.DataBase

import com.example.receapp.Clases.Alergenos
import com.example.receapp.Clases.Dificultad
import com.example.receapp.Clases.Receta
import com.example.receapp.Clases.Tipo

/**
 * Proveedor de recetas que almacena y proporciona una lista de recetas predefinidas.
 */

class RecetaProvider {
    companion object{
        /**
         * Lista de recetas predefinidas.
         */
        var recetas = listOf(
            Receta(
                nombre = "Tarta de Queso",
                dificultad = Dificultad.Fácil,
                imagen = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSH9ZlaEPDMVaHfYlhrlp7Ztrj1BzFRoH2Vg&usqp=CAU",
                ingredientes = listOf(
                    "900 gramos de queso crema",
                    "5 huevos",
                    "300 gramos de azúcar",
                    "435 gramos de nata para montar",
                    "1 cucharada de azúcar"
                ),
                pasos = listOf(
                    "Precalentar el horno a 180 grados con calor arriba y abajo.",
                    "Mezclar bien todos los ingredientes para que no queden grumos.",
                    "Introducir la mezcla en un molde de 25 cm con papel de horno.",
                    "Hornear durante 40 minutos.",
                    "Dejar reposar dentro del horno apagado durante 1 hora.",
                    "Guardar en la nevera cuando la tarta esté fría y esperar hasta el dia siguiente para consumir."
                ),
                duracion = 120,
                tipo = Tipo.Postre,
                alergenos = listOf(
                    Alergenos.Gluten,
                    Alergenos.Huevos
                )
            ),
            Receta(
                nombre = "Lasaña de pera, piñones y queso mascarpone",
                dificultad = Dificultad.Media,
                imagen = "https://canalcocina.es/medias/_cache/zoom-7cc62e88e3fa2ebecd81c0f72e1daaee-920-518.jpg",
                ingredientes = listOf(
                    "8 láminas de pasta seca para canelones",
                    "1 tallo de apio lavado",
                    "1 puerro lavado ",
                    "2 peras conferencia",
                    "50 g de piñones",
                    "30 ml de vino blanco seco",
                    "100 g de queso mascarpone",
                    "50 g de queso manchego rallado",
                    "Albahaca baby fresca",
                    "Para la salsa: ",
                    "25 g de mantequilla ",
                    "25 g de harina de trigo",
                    "300 ml de leche",
                    "Sal",
                    "Pimienta blanca",
                    "Nuez moscada"
                ),
                pasos = listOf(
                    "Para la salsa calentamos la mantequilla en una sartén, añadimos la harina y la tostamos ligeramente.",
                    "Agregamos la leche y removemos para incorporar. Condimentamos con sal, pimienta blanca y nuez moscada",
                    "Cocemos 10 minutos, removiendo de vez en cuando, o hasta que la salsa espese",
                    "Al mismo tiempo preparamos el relleno. Pelamos el apio, lo picamos y pochamos en una sartén con un fondo de aceite.",
                    "Cortamos el puerro en tiras y lo agregamos a la sartén. Pelamos y cortamos las peras en dados y las incorporamos junto con los piñones.",
                    "Después de un par de minutos regamos con el vino y dejamos evaporar el alcohol.",
                    "Fuera del fuego añadimos el queso mascarpone, salpimentamos al gusto y reservamos.",
                    "Cocemos las láminas de pasta en agua con sal un par de minutos menos del tiempo que indique el fabricante.",
                    "Escurrimos y colocamos sobre un paño de cocina para retirar el exceso de agua.",
                    "Introducimos la bandeja en el horno, pre calentado a 220 ºC en función grill, y doramos (al tiempo que calentamos) durante 5-6 minutos."
                ),
                duracion = 60,
                tipo = Tipo.Principal,
                alergenos = listOf(
                    Alergenos.Frutos,
                    Alergenos.Huevos,
                    Alergenos.Gluten
                )
            ),
            Receta(
                nombre = "Strudel de pollo, chammpiñones y manzana con ensalada de hierbas al limón",
                dificultad = Dificultad.Media,
                imagen = "https://canalcocina.es/medias/_cache/zoom-1fd43536d86755386b3aa79ba8e491eb-920-518.jpg",
                ingredientes = listOf(
                    "400 g de champiñones",
                    "2 puerros",
                    "500 g de pechugas de pollo picadas",
                    "300 ml de caldo de ave",
                    "1 manzana reineta",
                    "5-6 ramas de perejil fresco",
                    "50 g de uvas pasas",
                    "2 huevos",
                    "100 g de virutas de jamón serrano",
                    "7 láminas de masa filo",
                    "80 g de mantequilla",
                    "Aceite de oliva virgen extra",
                    "Sal",
                    "Pimienta negra",
                    "Semillas de sésamo",
                    "Para la guarnición: ",
                    "100 g de mezcla de hojas verdes",
                    "1 rama de perejil fresco",
                    "1 rama de menta fresca",
                    "1 rama de cilantro fresco",
                    "1 rama de albahaca fresca",
                    "60 ml de aceite de oliva extra virgen",
                    "1/2 Limón",
                    "1 diente de ajo pelado",
                    "Sal",
                    "Pimienta negra"
                ),
                pasos = listOf(
                    "Lavamos bien los puerros y los cortamos en discos finos y los salteamos en una cazuela.",
                    "Añadimos los champiñones laminados y salpimentamos.",
                    "Incorporamos la carne de pollo picada.",
                    "Rehogamos hasta que dore.",
                    "Regamos con el caldo y cocemos unos 10 minutos o hasta que reduzca el líquido.",
                    "Escurrimos bien y lo incorporamos a un bol.",
                    "Añadimos la manzana pelada y picada, las uvas pasas, los huevos, las virutas de jamón y el perejil bien picado.",
                    "Fundimos la mantequilla y le agregamos el aceite de girasol.",
                    "Extendemos una primera lámina de masa filo sobre papel vegetal y la pincelamos con la mezcla de mantequilla y aceite. Repetimos la operación con las láminas restantes.",
                    "Extendemos el relleno sobre la masa filo, dejando unos centímetros libres en todo el contorno. Doblamos los bordes más estrechos sobre el relleno y enrollamos desde el borde más largo.",
                    "Transferimos a una bandeja de horno. Pincelamos con el resto de la mezcla de mantequilla y aceite, espolvoreamos con semillas de sésamo y cocemos en el horno, precalentado a 200 ºC, durante 35 minutos o hasta que se vea dorado.",
                    "Mientras tanto, lavamos las hojas verdes, eliminamos los tallos gruesos y secamos. Picamos las hojas de las hierbas frescas y las mezclamos con las lechugas. Mezclamos el aceite, el zumo de 1/2 limón, 1/2 ajo rallado, sal y pimienta en un bol. Vertemos sobre la ensalada y removemos."
                ),
                duracion = 100,
                tipo = Tipo.Principal,
                alergenos = listOf(
                    Alergenos.Huevos,
                    Alergenos.Gluten
                )
            ),
            Receta(
                nombre = "Verdinas con marisco",
                dificultad = Dificultad.Fácil,
                imagen = "https://canalcocina.es/medias/_cache/zoom-a9119abb9bd5c578d7bf585c40fe607b-920-518.jpg",
                ingredientes = listOf(
                    "250 g de verdinas, remojadas en agua fría durante 12 horas",
                    "250 g de almejas purgadas",
                    "1 hoja de laurel",
                    "1 puerro",
                    "3 dientes de ajo pelados",
                    "3 cucharadas de salsa de tomate",
                    "50 ml de vino de jerez",
                    "300 ml de caldo de pescado",
                    "2 carabineros",
                    "Perejil fresco",
                    "Aceite de oliva virgen",
                    "Sal"
                ),
                pasos = listOf(
                    "Remojamos las verdinas en agua fría durante 12 horas, las podemos poner la noche anterior.",
                    "Purgamos las almejas en un cuenco con abundante agua salada durante 2 horas. Las retiramos sin remover el agua y las reservamos.",
                    "Introducimos las verdinas en una cazuela, las cubrimos con agua y añadimos la hoja de laurel. Llevamos a ebullición a fuego fuerte, bajamos el fuego y desespumamos.",
                    "Picamos el puerro y lo pochamos en una sartén con un fondo de aceite. Agregamos los dientes de ajo pelados y troceados a la sartén y sofreímos hasta que el puerro esté tierno.",
                    "Agregamos la salsa de tomate y el vino de jerez. Cocemos a fuego alto para que se evapore el alcohol antes de regar con el caldo y llevar de nuevo a ebullición.",
                    "Trituramos y vertemos en la cazuela con las verdinas. Cocemos a fuego suave hasta que estén tiernas.",
                    "Apagamos el fuego, agregamos los carabineros enteros y las almejas, tapamos la cazuela y dejamos reposar 10 minutos antes de sazonar al gusto y servir. Los carabineros y las almejas se cuecen con el calor residual."
                ),
                duracion = 60,
                tipo = Tipo.Entrante,
                alergenos = listOf(
                    Alergenos.Marisco,
                    Alergenos.Gluten
                )
            ), Receta(
                nombre = "Codornices asadas con salteado de higos, manzana y romero",
                dificultad = Dificultad.Media,
                imagen = "https://canalcocina.es/medias/_cache/zoom-c73c4b6fa9f1ddbe98a91a3d0afc3cb8-920-518.jpg",
                ingredientes = listOf(
                    "4 codornices",
                    "4 higos secos",
                    "50 ml de coñac",
                    "4 brevas",
                    "4 higos frescos",
                    "2 manzanas Golden",
                    "100 g de azúcar",
                    "50 g de mantequilla",
                    "1 ramita de romero fresco",
                    "250 ml de Oporto",
                    "500 ml de caldo de carne",
                    "Sal",
                    "Pimienta"
                ),
                pasos = listOf(
                    "Salteamos las codornices en una sartén a fuego fuerte, dejándolas crudas por dentro y doraditas por fuera. Una vez templadas, las deshuesamos, extrayendo las pechugas enteras y dejando los muslitos con su hueso.",
                    "Troceamos el caparazón y lo doramos en la misma sartén hasta que esté bien doradito; retiramos el exceso de grasa e incorporamos el oporto, dejándolo reducir hasta casi seco: en ese momento agregamos el caldo y reducimos de nuevo hasta que tenga consistencia de salsa. Colamos y reservamos.",
                    "Pelamos las manzanas y las cortamos en dados de ½ cm.; los higos y las brevas los cortamos en 4 trozos.",
                    "Ponemos el azúcar y la mantequilla en una sartén a fuego medio y, cuando comience a hacerse un caramelo rubio, añadimos las manzanas, los higos y las brevas dejándolas hasta que se ablanden y tengan un color doradito, unos 15 minutos.",
                    "Salpimentamos las pechugas y muslitos de codorniz y las acabamos de hacer en una sartén, dejándolas jugosas.",
                    "Servimos la manzana, luego los higos y, finalmente, los trozos de codorniz. Regamos con un poco de salsa y decoramos con romero."
                ),
                duracion = 80,
                tipo = Tipo.Principal,
                alergenos = listOf(
                    Alergenos.Huevos
                )
            )

        )
    }
}