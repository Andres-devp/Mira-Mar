const navToggle = document.getElementById('nav-toggle');
const navMenu = document.getElementById('nav-menu');

if (navToggle && navMenu) {
  navToggle.addEventListener('click', () => {
    const isOpen = navMenu.classList.toggle('open');
    navToggle.setAttribute('aria-expanded', String(isOpen));
  });

  navMenu.querySelectorAll('a').forEach((link) => {
    link.addEventListener('click', () => {
      navMenu.classList.remove('open');
      navToggle.setAttribute('aria-expanded', 'false');
    });
  });
}

const serviceDetails = {
  restaurant: {
    title: 'Beachfront Restaurant',
    description:
      'Enjoy breakfast by sunrise, seafood specialties for lunch, and candlelit dinners with live island music every evening.'
  },
  spa: {
    title: 'Spa & Massage',
    description:
      'Choose from deep tissue, hot stone, and aloe-based treatments, all delivered in serene cabanas with ocean sounds.'
  },
  tours: {
    title: 'Island Tours',
    description:
      'Half-day and full-day itineraries include cultural stops, local artisan markets, scenic viewpoints, and nature trails.'
  },
  shuttle: {
    title: 'Airport Shuttle',
    description:
      'Scheduled and private transportation available from major nearby airports, with assistance for luggage and check-in.'
  },
  poolbar: {
    title: 'Pool Bar',
    description:
      'Sip handcrafted tropical drinks, fresh juices, and light bites in a laid-back lounge atmosphere from noon to late evening.'
  },
  watersports: {
    title: 'Snorkeling & Water Sports',
    description:
      'Certified guides provide equipment and safety briefings for snorkeling, paddleboarding, and beginner-friendly kayaking.'
  },
  coworking: {
    title: 'Coworking Lounge',
    description:
      'A calm, air-conditioned workspace with high-speed Wi-Fi, coffee station, meeting booths, and print support.'
  },
  gym: {
    title: 'Gym & Wellness Studio',
    description:
      'Cardio and strength equipment plus guided morning yoga and mobility sessions to keep your routine on track.'
  }
};

const modal = document.getElementById('service-modal');
const modalTitle = document.getElementById('modal-title');
const modalDescription = document.getElementById('modal-description');
const modalClose = document.getElementById('modal-close');
const modalContent = modal?.querySelector('.modal-content');
const serviceButtons = document.querySelectorAll('.view-service');
let previouslyFocusedElement = null;

const openModal = (serviceKey) => {
  if (!modal || !modalTitle || !modalDescription || !modalContent) return;
  const details = serviceDetails[serviceKey];
  if (!details) return;

  previouslyFocusedElement = document.activeElement;
  modalTitle.textContent = details.title;
  modalDescription.textContent = details.description;
  modal.hidden = false;
  modalContent.focus();
};

const closeModal = () => {
  if (!modal) return;
  modal.hidden = true;
  if (previouslyFocusedElement instanceof HTMLElement) {
    previouslyFocusedElement.focus();
  }
};

serviceButtons.forEach((button) => {
  button.addEventListener('click', (event) => {
    const card = event.currentTarget.closest('.service-card');
    const serviceKey = card?.dataset.service;
    if (serviceKey) {
      openModal(serviceKey);
    }
  });
});

modalClose?.addEventListener('click', closeModal);

modal?.addEventListener('click', (event) => {
  if (event.target === modal) {
    closeModal();
  }
});

document.addEventListener('keydown', (event) => {
  if (event.key === 'Escape' && modal && !modal.hidden) {
    closeModal();
  }
});

const bookingForm = document.getElementById('booking-form');
const bookingMessage = document.getElementById('booking-message');

bookingForm?.addEventListener('submit', (event) => {
  event.preventDefault();
  if (!bookingMessage) return;

  const checkIn = document.getElementById('checkin')?.value;
  const checkOut = document.getElementById('checkout')?.value;
  const guestsValue = document.getElementById('guests')?.value;
  const guests = Number(guestsValue);

  if (!checkIn || !checkOut) {
    bookingMessage.textContent = 'Please select both check-in and check-out dates.';
    bookingMessage.style.color = '#af3f3f';
    return;
  }

  if (new Date(checkOut) <= new Date(checkIn)) {
    bookingMessage.textContent = 'Check-out must be after check-in.';
    bookingMessage.style.color = '#af3f3f';
    return;
  }

  if (!Number.isFinite(guests) || guests <= 0) {
    bookingMessage.textContent = 'Please enter a valid number of guests.';
    bookingMessage.style.color = '#af3f3f';
    return;
  }

  bookingMessage.textContent = `Great choice! We found options for ${guests} guest${guests > 1 ? 's' : ''} from ${checkIn} to ${checkOut}.`;
  bookingMessage.style.color = '#127f6d';
});

const contactForm = document.getElementById('contact-form');
const contactMessage = document.getElementById('contact-message');

contactForm?.addEventListener('submit', (event) => {
  event.preventDefault();
  if (!contactMessage) return;

  const name = document.getElementById('name')?.value.trim();
  const email = document.getElementById('email')?.value.trim();
  const message = document.getElementById('message')?.value.trim();
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!name || !email || !message) {
    contactMessage.textContent = 'Please complete all contact fields before sending.';
    contactMessage.style.color = '#af3f3f';
    return;
  }

  if (!emailPattern.test(email)) {
    contactMessage.textContent = 'Please enter a valid email address.';
    contactMessage.style.color = '#af3f3f';
    return;
  }

  contactMessage.textContent = `Thanks, ${name}! Your message has been sent. We'll get back to you shortly.`;
  contactMessage.style.color = '#127f6d';
  contactForm.reset();
});
