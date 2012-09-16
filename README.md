playtemplatedemo
================

Demo how you can accomplish using external, hot reloadable templates using groovy in play 1.2.x - it is extracted from an internal project.

What does it do?
----------------

- it lets you define an external template path (the root where your templates live)
- you can provide multiple "templates" (directories) where it should look for it's files, the file is read from the first template-directory that contains the requested files. 
- when files are changed they are compiled

How do you use it?
-----------------
Probably you won't. You can use this code as a base, and enhance it to your needs.

Some implementation notes:
-------------------------
- since the default groovy implementation insists on rendering VFS-files (which only can exists within the play-root) I had to do some things to get it to work:
 - re-implement the #include and #extends tag (they are now called #theme.include and     #theme.extends
 - call the template loader directly from the controller (instead of calling render and using 
  the play-plugin to load the requested template)
- our internal implementation has more features, like serving static content and resizing images, but  I decided to remove them because it has quite some project related  business logic (and it distractes from the real issue I wanted to show: hot reloading + fallback)
- this code is not compatible with the faster groovy template module
- if you use hot-reloading, might also run into concurrency issues, caused by this issue: https://play.lighthouseapp.com/projects/57987/tickets/1589-concurrency-prpblem-with-groovy-template-compilation
- I removed our home-brew logger (comparable to the "better logs" module) and changed it to the play! logger, so loggin might be a bit verbose

A comparable implementation has been used very successfully at my company in a production environment, but of course there 
might be smarter/better ways to do this - so if you have any hints: let me know.

I hope this code helps somebody to get some inspiration from.




