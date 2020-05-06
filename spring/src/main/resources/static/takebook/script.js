
const URL_LIBRARY = "/takebook/rest/library/";
const URL_SHELF = "/takebook/rest/shelf/";
const URL_BOOK_BY_LIBRARY = "/takebook/rest/book/library/";
const URL_BOOK_BY_BORROWER = "/takebook/rest/book/borrower/";

const URL_BOOK_BORROW = "/takebook/rest/book/borrow/";
const URL_BOOK_PUT_AWAY = "/takebook/rest/book/putAway/";
const URL_BOOK_RETURN = "/takebook/rest/book/return/";

var detailsBookFunction = function(book) {
    switch(book.state) {
        case "BORROWED":
            return "Emprunté par " + book.borrower;
        case "RETURNED":
            return "Retourné à la librairie " + book.library.name;
        case "STORED":
            return "Rangé dans l'étagère " + book.shelf.label;
        default:
            return "";
    }
}

var libraryListPanel = new Vue({
    el: '#libraryListPanel',
    data: {
        list : []
    },
    created: function () {
        this.refresh();
    },
    methods: {
        refresh: function () {
            axios.get(URL_LIBRARY).then(function (response) {
                libraryListPanel.list = response.data;
            }).catch(function (error) {
                console.log(error);
            });
        },
        details: function(library) {
            libraryDetailsPanel.show(library);
        }
      }
});


var libraryDetailsPanel = new Vue({
    el: '#libraryDetailsPanel',
    data: {
        isShow : false,
        library : {},
        shelves : [],
        books : []
    },
    methods: {
        show: function(library) {
            this.library = library;
            this.isShow = true;
            this.refreshShelves();
            this.refreshBooks();

        },
        refreshShelves: function() {
            axios.post(URL_SHELF, libraryDetailsPanel.library)
            .then(function (response) {
                libraryDetailsPanel.shelves = response.data;
            }).catch(function (error) {
                console.log(error);
                libraryDetailsPanel.isShow = false;
            });
        },
        refreshBooks: function() {
            axios.post(URL_BOOK_BY_LIBRARY, libraryDetailsPanel.library)
            .then(function (response) {
                libraryDetailsPanel.books = response.data;
            }).catch(function (error) {
                console.log(error);
                libraryDetailsPanel.isShow = false;
            });
        },
        detailsBook: function(book) {
            return detailsBookFunction(book);
        },
        borrow : function(book) {
            var borrower = prompt("Merci de saisir la personne qui a emprunté le livre " + book.title + ":", "");
            if (borrower != null && borrower != "") {
                var request = {
                    book : book,
                    borrower : borrower
                };
                axios.post(URL_BOOK_BORROW, request)
                .then(function (response) {
                    libraryDetailsPanel.refreshBooks();
                }).catch(function (error) {
                    console.log(error);
                    libraryDetailsPanel.isShow = false;
                });
            }
        },
        putAway : function(book) {
            var shelvesLabel = "";
            libraryDetailsPanel.shelves.forEach(function(shelf) {
                if(shelvesLabel == "") {
                    shelvesLabel += shelf.label;
                } else {
                    shelvesLabel += ", " + shelf.label;
                }
            });
            var shelfLabel = prompt("Merci de saisir le libellé de l'étagère (" + shelvesLabel + ") où ranger le livre " + book.title + ":", "");
            if (shelfLabel != null && shelfLabel != "") {
                var shelf = this.findShelfByLabel(shelfLabel);
                if(shelf == null) {
                    alert("Le libellé de l'étagère n'est pas reconnu");
                } else {
                    var request = {
                        book : book,
                        shelf : shelf
                    };
                    axios.post(URL_BOOK_PUT_AWAY, request)
                    .then(function (response) {
                        libraryDetailsPanel.refreshBooks();
                    }).catch(function (error) {
                        console.log(error);
                        libraryDetailsPanel.isShow = false;
                    });
                }
            }
        },
        findShelfByLabel: function(label){
            var shelf = null;
            libraryDetailsPanel.shelves.forEach(function(each) {
                if(label == each.label) {
                    shelf = each;
                }
            });
            return shelf;
        },
        cancel: function() {
            this.isShow = false;
        }
    }
});


var borrowBookFormPanel = new Vue({
    el: '#borrowBookFormPanel',
    data: {
        borrower : "",
        books : []
    },
    methods: {
        search: function() {
            axios.post(URL_BOOK_BY_BORROWER, borrowBookFormPanel.borrower, {
                headers: {
                    'Content-type': 'text/plain'
                }
            })
            .then(function (response) {
                borrowBookFormPanel.books = response.data;
            }).catch(function (error) {
                console.log(error);
                userFormVue.isShow = false;
            });
        },
        detailsBook: function(book) {
            return detailsBookFunction(book);
        },
        ret: function(book) {
            var librariesNames = "";
            libraryListPanel.list.forEach(function(library) {
                if(librariesNames == "") {
                    librariesNames += library.name;
                } else {
                    librariesNames += ", " + library.name;
                }
            });
            var libraryName = prompt("Merci de saisir le nom de la librarie (" + librariesNames + ") où ramener le livre " + book.title + ":", "");
            if (libraryName != null && libraryName != "") {
                var library = this.findLibraryByName(libraryName);
                if(library == null) {
                    alert("Le nom de la librarie n'est pas reconnu");
                } else {
                    var request = {
                        book : book,
                        library : library
                    };
                    axios.post(URL_BOOK_RETURN, request)
                    .then(function (response) {
                        borrowBookFormPanel.search();
                    }).catch(function (error) {
                        console.log(error);
                    });
                }
            }
        },
        findLibraryByName: function(name){
            var library = null;
            libraryListPanel.list.forEach(function(each) {
                    if(name == each.name) {
                        library = each;
                    }
                });
            return library;
        }
    }
});