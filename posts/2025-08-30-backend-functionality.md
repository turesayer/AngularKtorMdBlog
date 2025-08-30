# Serving blog posts from the backend

After being off for the summer, I finally found some time to work on my blog 
engine again. So far I've only completed the first of the three steps I 
mentioned in the last post.

```markdown
There a bunch of features I'd like to add to my blog engine:
1. Saving the post in the backend. Probably even set it up in a way that you
   can push new blog posts without redeploying.
2. Finding a nice way of adding images
3. Adding a sidebar with an overview over all available post
```

## So what changed?
In short, posts are now served from a configurable backend directory. There 
must be a file called `metadata.json` in that directory containing the following
information for each post:
```json
[
  {
    "title": "Hello World",
    "date": "2025-06-14",
    "filename": "2025-06-14-hello-world.md",
    "visibility": "PUBLIC"
  },
  ...
]
```

The `title` property isn't used yet but will be for things like an overview list. 
`date` is used for sorting and is now displayed in the top-right corner of each 
post. `filename` should be self-explanatory, and `visibility` can hide posts 
from display.

If you build the Docker image directly from the repository, the `posts` 
directory will contain my blog posts. Anyone can also mount a directory with
their posts into the container, and the engine will pick them up dynamically 
without redeploying. Adding or editing posts is possible without redeploying; 
every change is visible immediately. This obviously comes with one minor 
downside: there is no caching. Every time someone loads the webpage, the 
metadata file and the requested posts are read from disk. As long as you're not
running a blog with thousands of posts and high traffic, this shouldn't be a 
problem—but it's something to be aware of.

## The details

I'll split this into three parts: backend, frontend, and quality-of-life 
improvements.

### Backend changes
The most important part is defining a directory where the blog posts are 
stored. This is passed to the app via `application.yaml`, with 
`MD_FILE_LOCATION` as an environment variable. For local development it's 
stored in `.env`.
```yaml
ktor:
  ...
  posts:
    file_location: "$MD_FILE_LOCATION:/posts"
...
```

From there, it was just two tasks: first, update the `/posts` endpoint to return
the list of posts based on the `metadata.json` and the files actually stored in 
the directory; second, add an endpoint to serve the content of a single post.

The post-list endpoint reads the metadata file, filters out posts that should 
not be displayed or whose files are missing, sorts by date, and returns the 
list. I used `kotlinx-serialization-json` to read the metadata and 
`kotlinx-datetime` to parse dates. I added two data classes: one matching the
metadata in the JSON file, and a DTO served to the frontend that omits the 
visibility flag, since the frontend only receives info about visible posts.

The single-post endpoint is simpler: it takes a filename, finds it in the posts
directory, and returns the file's full text. Besides a simple existence check, 
it just reads the file. This means that if someone knows a filename, they can
request its content even if it should be hidden; I'll fix this in an upcoming 
version.

### Frontend changes
The frontend changes are simple: I replaced the current list of posts with the
new format from the backend and added a service call to load a post's content. 
Because the text was already loaded into a variable, I only added a `div` 
showing the post date above the content. The only somewhat interesting change
was parsing the backend date into a JS Date object—see 
`frontend-data-api.service.ts` if you're interested.

### Quality of life improvements
On the frontend, I added prod and dev environments that automatically switch 
API paths depending on whether you're running Angular in hot-reload development
mode or serving the built app through Ktor. For the backend, I improved the 
Docker build and deploy process. First, you can now specify the Docker 
executable so you can use non-Docker Desktop installations like Podman. 
Second, I added everything needed to build and push the Docker image, including
the new `posts` directory.

## Final thoughts
I'm satisfied with the result so far. Next, I'll add some minor fixes, such as
checking a post's visibility before serving its content. Afterwards, I'll add a
simple way to include images in posts by using the existing posts directory. As
soon as this is done, I'll release version 0.1.0, so I can differentiate 
patches (incrementing only the last number) from features (incrementing the 
middle number while staying in the 0.x.y range).

As before you can find the code on GitHub: 
[AngularKtorMdBlog](https://github.com/turesayer/AngularKtorMdBlog)