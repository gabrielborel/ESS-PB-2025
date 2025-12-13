import { faker } from "@faker-js/faker";

const API_URL = "http://192.168.49.2:30080";
const DELAY = 100;

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

async function createBook() {
  const book = {
    title: faker.lorem.words({ min: 2, max: 5 }),
    author: faker.person.fullName(),
    publicationYear: faker.number.int({ min: 1900, max: 2024 }),
  };

  const res = await fetch(`${API_URL}/api/books`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(book),
  });

  if (!res.ok) throw new Error(`Failed to create book: ${res.status}`);
  return res.json();
}

async function createReview(bookId) {
  const review = {
    bookId,
    rating: faker.number.int({ min: 1, max: 5 }),
    comment: faker.lorem.sentence(),
    reviewerName: faker.person.fullName(),
  };

  const res = await fetch(`${API_URL}/api/reviews`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(review),
  });

  if (!res.ok) throw new Error(`Failed to create review: ${res.status}`);
  return res.json();
}

async function seed() {
  const BOOKS = 500;
  const REVIEWS = 1500;

  console.log(`Creating ${BOOKS} books...`);
  const bookIds = [];

  for (let i = 0; i < BOOKS; i++) {
    try {
      const book = await createBook();
      bookIds.push(book.id);
      if ((i + 1) % 50 === 0) console.log(`  ${i + 1}/${BOOKS} books`);
      await sleep(DELAY);
    } catch (e) {
      console.error(`Error creating book ${i}:`, e.message);
    }
  }

  console.log(`\nCreating ${REVIEWS} reviews...`);
  for (let i = 0; i < REVIEWS; i++) {
    try {
      const bookId = bookIds[Math.floor(Math.random() * bookIds.length)];
      await createReview(bookId);
      if ((i + 1) % 100 === 0) console.log(`  ${i + 1}/${REVIEWS} reviews`);
      await sleep(DELAY);
    } catch (e) {
      console.error(`Error creating review ${i}:`, e.message);
    }
  }

  console.log("Seeds script finished!");
}

seed();
