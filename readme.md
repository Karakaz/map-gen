## map-gen

A engine to dynamically create random maps from parameters for games like Risk

### Generation process:

Generate two voronoi graphs, the bigger one will be used to build continents, the smaller to build countries.

![Continent voronoi](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map1_1.png)

![Both voronois together](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map1_2.png)

Next step is to connect two voronoi polygons together to create continents

![Creating continents](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map1_3.png)

Lands are now created by combining polygons within continents

![Land creation](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map1_4.png)

Final map

![FInal map, similar colored lands are in the same continent](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map1_5.png)

### Other maps generated

![Map2](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map2.png)

![Map3](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map3.png)

![Map4](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map4.png)

![Map5](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map5.png)

![Map6](https://raw.githubusercontent.com/Karakaz/map-gen/master/examples/map6.png)
