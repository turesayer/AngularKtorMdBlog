# A simple blog engine build around Markdown files using Angular served by Ktor

As promised in my last post my first tech related blog entry will focus on the
current state of the blog engine I build to run this very blog you are 
currently reading. You can find links to the repo on the bottom of this post.

## Why did I start this project?

A few weeks ago I thought was tinkering around with some Angular related stuff
and had a pretty specific issue I could not solve myself and also none of the
usual AI services nor stack overflow provided me sufficient hints to overcome
my issue. After browsing the internet for quite some time I stumbled on some
hobbiyst blog and in there someone actually described the very issue I was 
fighting with. So I thought, hmmm, why don't I start writing a bit about my own
hobby projects, maybe from time to time someone might come around and actually
find something usful in these writeups.

At the same weekend I was also reading something about 
[Ktor](https://ktor.io/), a Kotlin framework made by Jetbrains, mainly to 
develop asynchronous server applications and discovered in their documentation
a fascinating functionality for a hobbyist like myself. A build in 
functionality to serve single-page applications
([Have look at their docs](https://ktor.io/docs/server-serving-spa.html)). It 
doesn't require any sophisticated setup, it's mainly copying over the dist 
folder of your spa and adding four lines of code to your Ktor routing setup.

```kotlin
fun Application.configureRouting() {
    routing {
        singlePageApplication {
            useResources = true
            angular("frontend/browser")  // a location within your resources folder
        }
    }
}
```

If you want to deploy your web app as simple as possible, this is a super nice
feature since you can just write your backend and have it serve the frontend
without the need of an additional server or whatever to host your frontend.
Sure, you can also do this in Spring or Flask or NestJS, but I've never seen it
wrapped that nicely. 

So reading this and having thought about publishing some of my own work I 
thought I anyway wanted to explore Ktor a bit more ever since I toyed around
with it three years ago, so let's just create a minimal Angular frontend that
displays my blogposts and have it served via Ktor. Assuming this is not going
to need the createst performance, I can just implement backend functionality as
I go on and deploy it all in a single container on my private server. So I sat
down a wrote the first iteration of this blog engine. So far the backend
actually does nothing besides serving the frontend and the posts are backed
into the Angular app as static content. I don't even know if serving images 
would work reasonably right now.

## The Angular app

Since I orginally wanted to focus on Ktor and not too much on Angular, I just
imported a package called 
[ngx-markdown](https://www.npmjs.com/package/ngx-markdown). It seems to contain
all the functionality I needed, rendering Markdown, a decent code highlighting
if you hook it up with [PrismJS](https://www.npmjs.com/package/prismjs) and a
lot of other things I didn't really care about. Beside that it seems to be 
actively maintained, something I thought could be quite important, since I 
don't want to spend a lot of hours later on switching it out when it deprecates
into nowhere a year down the line.

`PrismJS` makes it super easy to just put Markdown content into any site:
```html
<markdown [data]="markdown()"></markdown>
```
The content of the data field just looks like a function since I put the 
Markdown data into a signal to be able to load it dynamically.

After having that set up I could already see some content in my browser. To
make it somewhat modular and prepare it for the future I decided to create a
component called `blog-post` that has a class variable annotated with 
`@Input()` containing the location of the Markdown file. This way I can just
dynamically create an instance of this component for each blog post. If you're
interested in how to load the Markdown content have a look at `blog-post.ts`
it's just 54 lines of code including imports. 

To keep the rest as simple as possible there is just one more component with
the not very useful name `homepage`. Its Typescript file contains a list of the
blog posts, the order of this list also defines the order in which they are 
shown later on. You can see its current structure in the snippet below, the 
title property is not actually used right now. I added it in the beginning and
decided to keep it then anyways since it could be used for some kind of 
overview list later on.

```typescript
export class Homepage {
  blogPosts = [
    { title: 'A Simple Blog Engine', path: '/content/2025-06-15-base-blog-engine.md' },
    { title: 'Hello World', path: '/content/2025-06-14-hello-world.md' }
  ]
}
```
Its HTML file contains the title bar and then a for loop iterating through the
entries of the `blogPosts` list.
```html
@for (blogPostPath of blogPosts; track blogPostPath.path) {
     <app-blog-post [blogPostPath]="blogPostPath.path"></app-blog-post>
}
```
And those are already the key things I did. For convenience, I added a script
to the `package.json` file that builds the Angular app and copies it content 
over to the correct folder in the backend project to have it served via Ktor.
```shell
ng build && 
rimraf ../backend/src/main/resources/frontend/browser &&
copyfiles -u 2 dist/frontend/**/* ../backend/src/main/resources/frontend &&
copyfiles -u 1 dist/frontend/browser/**/* ../backend/src/main/resources/
```
I utilized `rimraf` and `copyfiles` here instead of native commands to keep it
as platform-agnostic as possible. So even though I created this on a Mac it
should run on Linux and Windows without any problems.

## The Ktor backend

Since there is no backend functionality so far and the Ktor setup is pretty
self-explanatory, I'm not creating yet another writeup of it. The serving of
the static web app is already covered by the code snippet above. Nevertheless
I discovered another cool feature. Premade Gradle tasks to build a Docker
image and if desired, push it to your local registry or any remote registry
([Link to the docs](https://ktor.io/docs/docker.html)).

I immediately wanted to have this, so I prepared a location on my git server
(it also runs a container registry) to push the image to and set it up. The
only thing needed was to add a few lines to the `build.gradle.kts`. I did it
like this:
```kotlin
ktor {
    docker {
        localImageName.set("blogging_engine")
        imageTag.set(project.version.toString())
        externalRegistry.set(
            DockerImageRegistry.externalRegistry(
                username = providers.environmentVariable("DOCKER_USER"),
                password = providers.environmentVariable("DOCKER_PW"),
                project = providers.environmentVariable("DOCKER_PROJECT"),
                hostname = providers.environmentVariable("DOCKER_HOST"),
            )
        )
    }
}
```

At
first, I got weird errors about IntelliJ not being able to find Docker on my
machine, which lead me to the discovery that IntelliJ doesn't have access to
the full PATH. I wasn't able to figure out how to solve it so far. I just found
the workaround to start IntelliJ via the terminal using the command below:
```shell
open -na "IntelliJ IDEA Ultimate.app"
```
It's not the most ergonomic workflow, so if you have any better idea, let me
know in the GitHub repo linked below.

## Current state
So with all the stuff above, I'm able to write a new post in a Markdown file. I
put it into the content folder in the Angular app. At this point I can already
preview it when I run the Angluar dev server. If I want to deploy I just run
the `build` script, it builds the Angular app and copies it together with the
data in the content server to folder in the backend project that is being 
served by Ktor. In the backend project I'm running the Gradle task called
`publishImages`, it builds and pushes the image to my server container 
registry. From there I can deploy it to my server. So it's far from being
automated but quite doable for a half-a-day project.

## What's next
There a bunch of features I'd like to add to my blog engine:
1. Saving the post in the backend. Probably even set it up in a way that you
   can push new blog posts without redeploying.
2. Finding a nice way of adding images
3. Adding a sidebar with an overview over all available post

Besides that, I want to polish an app I wrote for my office that remindes me 
and my colleagues to water our office plants regularly. Currently the code is
an overly complicated mess. It consists out of a Spring Boot backend saving 
data to a PostgreSQL and a MinIO file storage. The frontend is _again_ written 
in Angular but with two different component libraries, partly Material, partly
PrimeNG. The whole thing is then secured via a self-hosted Keycloak that's
hooked up to our companies SSO. I would like to simplify it, and then either
my next post will be about an update to my blog engine or a presentation
of my plant watering reminder app.

## The code:
GitHub: [AngularKtorMdBlog](https://github.com/turesayer/AngularKtorMdBlog)
