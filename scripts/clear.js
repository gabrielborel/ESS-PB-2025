const API_URL = "http://192.168.49.2:30080";
const DELAY = 50;

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

async function getAllBooks() {
  const res = await fetch(`${API_URL}/api/books`);
  if (!res.ok) throw new Error(`Failed to get books: ${res.status}`);
  return res.json();
}

async function deleteBook(id) {
  const res = await fetch(`${API_URL}/api/books/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error(`Failed to delete book ${id}: ${res.status}`);
}

async function clear() {
  console.log("Fetching books...");
  const books = await getAllBooks();
  console.log(`Found ${books.length} books. Deleting...`);

  for (let i = 0; i < books.length; i++) {
    try {
      await deleteBook(books[i].id);
      if ((i + 1) % 50 === 0) console.log(`  ${i + 1}/${books.length} deleted`);
      await sleep(DELAY);
    } catch (e) {
      console.error(`Error deleting book ${books[i].id}:`, e.message);
    }
  }

  console.log("\nDone!");
}

clear();
