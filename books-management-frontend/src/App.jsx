import { useState, useEffect } from 'react'
import BookList from './components/BookList'
import BookForm from './components/BookForm'
import ConfirmDeleteModal from './components/ConfirmDeleteModal'
import BookHistoryModal from './components/BookHistoryModal'
import ReviewModal from './components/ReviewModal'
import { getAllBooks, createBook, updateBook, deleteBook } from './services/bookService'

function App() {
  const [books, setBooks] = useState([])
  const [editingBook, setEditingBook] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const [deleteModal, setDeleteModal] = useState({ isOpen: false, book: null })
  const [historyModal, setHistoryModal] = useState({ isOpen: false, bookId: null })
  const [reviewModal, setReviewModal] = useState({ isOpen: false, book: null })

  useEffect(() => {
    loadBooks()
  }, [])

  const loadBooks = async () => {
    try {
      setLoading(true)
      setError(null)
      const data = await getAllBooks()
      setBooks(data)
    } catch (err) {
      setError('Erro ao carregar livros: ' + err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleCreateBook = async (bookData) => {
    try {
      setError(null)
      await createBook(bookData)
      await loadBooks()
    } catch (err) {
      setError('Erro ao criar livro: ' + err.message)
      throw err
    }
  }

  const handleUpdateBook = async (id, bookData) => {
    try {
      setError(null)
      await updateBook(id, bookData)
      await loadBooks()
      setEditingBook(null)
    } catch (err) {
      setError('Erro ao atualizar livro: ' + err.message)
      throw err
    }
  }

  const handleDeleteBook = (book) => {
    setDeleteModal({ isOpen: true, book })
  }

  const confirmDelete = async () => {
    try {
      setError(null)
      await deleteBook(deleteModal.book.id)
      await loadBooks()
      setDeleteModal({ isOpen: false, book: null })
    } catch (err) {
      setError('Erro ao excluir livro: ' + err.message)
      setDeleteModal({ isOpen: false, book: null })
    }
  }

  const cancelDelete = () => {
    setDeleteModal({ isOpen: false, book: null })
  }

  const handleEdit = (book) => {
    setEditingBook(book)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleCancelEdit = () => {
    setEditingBook(null)
  }

  const handleViewHistory = (bookId) => {
    setHistoryModal({ isOpen: true, bookId })
  }

  const closeHistoryModal = () => {
    setHistoryModal({ isOpen: false, bookId: null })
  }

  const handleViewReviews = (book) => {
    setReviewModal({ isOpen: true, book })
  }

  const closeReviewModal = () => {
    setReviewModal({ isOpen: false, book: null })
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <h1 className="text-3xl font-bold text-gray-900 mb-8">
            Gerenciamento de Livros
          </h1>

          {error && (
            <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded relative">
              <span className="block sm:inline">{error}</span>
              <button
                onClick={() => setError(null)}
                className="absolute top-0 bottom-0 right-0 px-4 py-3"
              >
                <span className="text-2xl">&times;</span>
              </button>
            </div>
          )}

          <div className="mb-8">
            <BookForm
              onSubmit={editingBook ? handleUpdateBook : handleCreateBook}
              editingBook={editingBook}
              onCancel={handleCancelEdit}
            />
          </div>

          <BookList
            books={books}
            onEdit={handleEdit}
            onDelete={handleDeleteBook}
            onViewHistory={handleViewHistory}
            onViewReviews={handleViewReviews}
            loading={loading}
          />
        </div>
      </div>

      <ConfirmDeleteModal
        isOpen={deleteModal.isOpen}
        onClose={cancelDelete}
        onConfirm={confirmDelete}
        bookTitle={deleteModal.book?.title || ''}
      />

      <BookHistoryModal
        bookId={historyModal.bookId}
        isOpen={historyModal.isOpen}
        onClose={closeHistoryModal}
      />

      {reviewModal.isOpen && (
        <ReviewModal
          book={reviewModal.book}
          onClose={closeReviewModal}
        />
      )}
    </div>
  )
}

export default App
