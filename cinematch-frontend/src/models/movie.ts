export class Movie {

    private _id: number;

    private _title: string;

    private _voteAverage: number;


    private _voteCount: number;

    
    private _status: string;


    private _revenue: number;


    private _budget: number;


    private _imdbId: string;


    private _originalLanguage: string;


    private _originalTitle: string;


    private _popularity: number;


    private _productionCompanies: string[];


    private _productionCountries: string[];

    private _spokenLanguages: string[];


    private _cast: string[];


    private _director: string[];


    private _directorOfPhotography: string[];


    private _writers: string[];


    private _producers: string[];


    private _musicComposer: string[];


    private _overview: string;

    private _releaseDate: Date;

    private _posterPath: string;

    private _genres: string[];

    private _imdbRating: number;

    private _imdbVotes: number;


    private _runtime: number;

    private _backdropPath: string;

    constructor() {

    }

    public get id(): number {
        return this._id;
    }
    public set id(value: number) {
        this._id = value;
    }
    public get title(): string {
        return this._title;
    }
    public set title(value: string) {
        this._title = value;
    }
    public get overview(): string {
        return this._overview;
    }
    public set overview(value: string) {
        this._overview = value;
    }
    public get releaseDate(): Date {
        return this._releaseDate;
    }
    public set releaseDate(value: Date) {
        this._releaseDate = value;
    }
    public get posterPath(): string {
        return this._posterPath;
    }
    public set posterPath(value: string) {
        this._posterPath = value;
    }

    public get genres(): string[] {
        return this._genres;
    }
    public set genres(value: string[]) {
        this._genres = value;
    }

    public get imdbRating(): number {
        return this._imdbRating;
    }
    public set imdbRating(value: number) {
        this._imdbRating = value;
    }

    public get runtime(): number {
        return this._runtime;
    }
    public set runtime(value: number) {
        this._runtime = value;
    }
    
    public get musicComposer(): string[] {
        return this._musicComposer;
    }
    public set musicComposer(value: string[]) {
        this._musicComposer = value;
    }
    public get voteAverage(): number {
        return this._voteAverage;
    }
    public set voteAverage(value: number) {
        this._voteAverage = value;
    }
    public get voteCount(): number {
        return this._voteCount;
    }
    public set voteCount(value: number) {
        this._voteCount = value;
    }
    public get status(): string {
        return this._status;
    }
    public set status(value: string) {
        this._status = value;
    }
    public get revenue(): number {
        return this._revenue;
    }
    public set revenue(value: number) {
        this._revenue = value;
    }
    public get budget(): number {
        return this._budget;
    }
    public set budget(value: number) {
        this._budget = value;
    }

    public get imdbId(): string {
        return this._imdbId;
    }
    public set imdbId(value: string) {
        this._imdbId = value;
    }

    public get originalLanguage(): string {
        return this._originalLanguage;
    }
    public set originalLanguage(value: string) {
        this._originalLanguage = value;
    }

    public get originalTitle(): string {
        return this._originalTitle;
    }
    public set originalTitle(value: string) {
        this._originalTitle = value;
    }
    public get popularity(): number {
        return this._popularity;
    }
    public set popularity(value: number) {
        this._popularity = value;
    }
    public get productionCompanies(): string[] {
        return this._productionCompanies;
    }
    public set productionCompanies(value: string[]) {
        this._productionCompanies = value;
    }
    public get productionCountries(): string[] {
        return this._productionCountries;
    }
    public set productionCountries(value: string[]) {
        this._productionCountries = value;
    }
    public get spokenLanguages(): string[] {
        return this._spokenLanguages;
    }
    public set spokenLanguages(value: string[]) {
        this._spokenLanguages = value;
    }
    public get cast(): string[] {
        return this._cast;
    }
    public set cast(value: string[]) {
        this._cast = value;
    }
    public get director(): string[] {
        return this._director;
    }
    public set director(value: string[]) {
        this._director = value;
    }
    public get writers(): string[] {
        return this._writers;
    }
    public set writers(value: string[]) {
        this._writers = value;
    }
    public get directorOfPhotography(): string[] {
        return this._directorOfPhotography;
    }
    public set directorOfPhotography(value: string[]) {
        this._directorOfPhotography = value;
    }

    public get producers(): string[] {
        return this._producers;
    }
    public set producers(value: string[]) {
        this._producers = value;
    }

    public get imdbVotes(): number {
        return this._imdbVotes;
    }
    public set imdbVotes(value: number) {
        this._imdbVotes = value;
    }
    public get backdropPath(): string {
        return this._backdropPath;
    }
    public set backdropPath(value: string) {
        this._backdropPath = value;
    }

}