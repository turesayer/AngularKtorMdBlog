# What is this?
This should become a blogging engine for blogs where each blog entry is a seperate markdown file. Technically the plan is to have the frontend written in Angular and serve it through Ktor's `Serving single-page applications` functionallity. This should also provide the opportunity to add backend functionallity if required in the lightest possible way without having to add a second server.

## How to run it?
Frontend is build using Angular 20 and Node 24. Probably it runs on 22 as well. Just go there
run `npm install` and you should be set. If you want to add your own blog post put them in the content folder and link them `homepage.ts`. To check if it works run `npm run serve`.
You should now be able to see the whole thing running on `localhost:4200`.

To make it deployable run `npm run build` it builds the frontend and copies the result to the designated location in the backend.

The backend is a gradle project using Kotlin and Ktor. If you open it with IntelliJ it should 
set up it self more or less. Probably you have to install a JDK if it's not already there.  Usually IntelliJ takes care of this or at leasts prompts you with reasonable default suggestions.

If you built the frontend before you can now just run the backend and again see the blog at `localhost:8080`. If you want to deploy this whole thing as a docker container there are gradle task to build and even publish the container. Just have a look a `build.gradle.kts` if you want to publish the container somewhere you need to set some env variables defining the target registry. If you've build the container succesfull you can deploy it anywhere.

If you want to add a new blog post, you need to again add it to the frontend, run `npm run build` then rebuild the container and update your deployment. There will be a more ergonomical flow in the future.