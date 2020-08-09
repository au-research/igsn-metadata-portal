'use strict'
const gulp = require('gulp')
const purgecss = require('gulp-purgecss')
const uglifycss = require('gulp-uglifycss')
const watch = require('gulp-watch')
const environments = require('gulp-environments')
const production = environments.production
const browserify = require('browserify')
const babelify = require('babelify')
const source = require('vinyl-source-stream')
const buffer = require('vinyl-buffer')
const sourcemaps = require('gulp-sourcemaps')
const uglify = require('gulp-terser')
const rename = require('gulp-rename')

gulp.task('css', function () {
  const postcss = require('gulp-postcss')

  return gulp.src('src/main/resources/css/portal.css')
    .pipe(postcss([
      require('postcss-import'),
      require('tailwindcss'),
      require('autoprefixer'),
    ]))
    .pipe(production(purgecss({
      content: ['src/main/resources/templates/**/*.html'],
      defaultExtractor: content => content.match(/[A-Za-z0-9-_:/]+/g) || []
    })))
    .pipe(production(uglifycss()))
    .pipe(rename('bundle.css'))
    .pipe(gulp.dest('src/main/resources/static/css/'))
})

gulp.task('copy-template', function () {
  return gulp.src('src/main/resources/templates/**/*.html').pipe(gulp.dest('target/classes/templates/'))
})

gulp.task('copy-static', function () {
  return gulp.src('src/main/resources/static/**/*').pipe(gulp.dest('target/classes/static/'))
})

const libs = [
  'highlight.js',
]

gulp.task('js-libs', function () {
  let b = browserify({
    debug: true
  })

  libs.forEach(function (lib) {
    b.require(lib)
  })

  return b.bundle()
    .pipe(source('vendor.js'))
    .pipe(buffer())
    .pipe(sourcemaps.init())
    .pipe(uglify())
    .pipe(sourcemaps.write('./maps'))
    .pipe(gulp.dest('./src/main/resources/static/js/'))
})

gulp.task('js', function () {
  let b = browserify({
    entries: ['./src/main/resources/js/app.js']
  })

  libs.forEach(function (lib) {
    b.external(lib)
  })

  return b
    .transform(babelify.configure({
      presets: ['@babel/preset-env']
    }))
    .bundle()
    .pipe(source('bundle.js'))
    .pipe(buffer())
    .pipe(sourcemaps.init())
    .pipe(production(uglify()))
    .pipe(sourcemaps.write('./maps'))
    .pipe(gulp.dest('./src/main/resources/static/js/'))
})

gulp.task('watch', function () {
  watch('src/main/resources/css/**/*.css', gulp.series('css'))
  watch('src/main/resources/js/**/*.js', gulp.series('js'))
  watch('src/main/resources/static/**/*', gulp.series('copy-static'))
  watch('src/main/resources/templates/**/*.html', gulp.series('copy-template'))
})

gulp.task('build', gulp.series('copy-template', 'css', 'js-libs', 'js', 'copy-static'))