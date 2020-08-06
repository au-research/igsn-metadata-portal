const gulp = require('gulp')
const purgecss = require('gulp-purgecss')
const uglifycss = require('gulp-uglifycss')
const watch = require('gulp-watch')
const environments = require('gulp-environments');
const production = environments.production;

gulp.task('css', function () {
  const postcss = require('gulp-postcss')

  return gulp.src('src/main/resources/css/portal.css')
    .pipe(postcss([
      require('tailwindcss'),
      require('autoprefixer'),
    ]))
    .pipe(production(purgecss({
      content: ['src/main/resources/templates/**/*.html']
    })))
    .pipe(production(uglifycss()))
    .pipe(gulp.dest('src/main/resources/static/css/'))
})

gulp.task('copy-template', function () {
  return gulp.src('src/main/resources/templates/*.html').pipe(gulp.dest('target/classes/templates/'));
})

gulp.task('copy-style', function () {
  return gulp.src('src/main/resources/static/**/*.css').pipe(gulp.dest('target/classes/static/'));
})

gulp.task('watch', function () {
  watch('src/main/resources/css/portal.css', gulp.series('css'))
  watch('src/main/resources/static/**/*.css', gulp.series('copy-style'))
  watch('src/main/resources/templates/*.html', gulp.series('copy-template'))
});

gulp.task('build', gulp.series('copy-template', 'css', 'copy-style'));