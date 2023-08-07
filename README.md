# Reagent Calculator 

This is a practice project in creating a calcular component with Reagent which is a React wrapper in ClojureScript.

# Hacking

This is an NPM project which uses Shadow CLJS. You can compile the ClojureScript into JavaScript by running

``` sh
npx shadow-cljs compile app
```

You can also start a watch server by running.

``` sh
npx shadow-cljs watch app
```

And then you can start hacking on the `src/reagent_calculator/main.cljs` file. It should start a HTTP server on port 8080 although there isn't a HTML page provided (see below). When you edit the code file, the webpage should automatically reload.

The project will need to be included in a HTML file by adding this tag in the body.

``` html
<script src="main.js"></script>
```

If the path to the JS file is different then this should be reflected in the `src` attribute. 

# Styles

As for styles, I used [Water CSS](https://watercss.kognise.dev/) styles but you should be able to use anything. This includes the default styles though I suspect they won't look great.
