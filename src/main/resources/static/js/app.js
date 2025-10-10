// Global variables
let statusChart = null;
let revenueChart = null;
let currentData = {
    airlines: [],
    terminals: [],
    routes: [],
    flights: [],
    passengers: [],
    aircraft: [],
    tickets: []
};

// API Base URL
const API_BASE_URL = '/api';

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    setupEventListeners();
    loadDashboardData();
    showSection('dashboard');
}

function setupEventListeners() {
    // Form submissions
    document.getElementById('airline-form').addEventListener('submit', handleAirlineSubmit);
    document.getElementById('terminal-form').addEventListener('submit', handleTerminalSubmit);
    document.getElementById('route-form').addEventListener('submit', handleRouteSubmit);
    document.getElementById('passenger-form').addEventListener('submit', handlePassengerSubmit);
    document.getElementById('aircraft-form').addEventListener('submit', handleAircraftSubmit);
    document.getElementById('ticket-form').addEventListener('submit', handleTicketSubmit);

    // Modal close on outside click
    window.addEventListener('click', function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    });
}

// Navigation functions
function showSection(sectionId) {
    console.log('Showing section:', sectionId);

    // Hide all sections
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => section.classList.remove('active'));

    // Show selected section
    document.getElementById(sectionId).classList.add('active');

    // Update navigation
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => link.classList.remove('active'));
    const activeLink = document.querySelector(`[onclick="showSection('${sectionId}')"]`);
    if (activeLink) activeLink.classList.add('active');

    // Load section-specific data
    switch(sectionId) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'airlines':
            loadAirlines();
            break;
        case 'terminals':
            loadTerminals();
            break;
        case 'routes':
            loadRoutes();
            break;
        case 'flights':
            loadFlights();
            break;
        case 'passengers':
            loadPassengers();
            break;
        case 'aircraft':
            loadAircraft();
            break;
        case 'tickets':
            loadTickets();
            break;
    }
}

// Search functionality
function searchTable(section) {
    const searchInput = document.getElementById(`${section}-search`);
    const filter = searchInput.value.toLowerCase();
    let tbody;

    if (section === 'routes') {
        tbody = document.getElementById('routes-tbody-management');
    } else {
        tbody = document.getElementById(`${section}-tbody`);
    }

    if (!tbody) {
        console.error('Table body not found for:', section);
        return;
    }

    const rows = tbody.getElementsByTagName('tr');

    Array.from(rows).forEach(row => {
        const text = row.textContent.toLowerCase();
        if (text.includes(filter)) {
            row.style.display = '';
            if (filter.length > 0) {
                row.classList.add('highlight');
                setTimeout(() => row.classList.remove('highlight'), 2000);
            }
        } else {
            row.style.display = 'none';
        }
    });
}

// Modal functions
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
    const form = document.querySelector(`#${modalId} form`);
    if (form) form.reset();
}

// Loading functions
function showLoading() {
    document.getElementById('loading').style.display = 'block';
}

function hideLoading() {
    document.getElementById('loading').style.display = 'none';
}

// API functions
async function apiCall(endpoint, method = 'GET', data = null) {
    showLoading();
    try {
        const config = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            }
        };

        if (data) {
            config.body = JSON.stringify(data);
        }

        console.log(`API Call: ${method} ${API_BASE_URL}${endpoint}`);
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        console.log(`API Response for ${endpoint}:`, result);
        return result;
    } catch (error) {
        console.error('API call failed:', error);
        showNotification('Error: ' + error.message, 'error');
        return null;
    } finally {
        hideLoading();
    }
}

// Dashboard functions
async function loadDashboardData() {
    try {
        const stats = await apiCall('/stats/dashboard');
        if (stats) {
            updateDashboardStats(stats);
            updateCharts(stats);
            updateBusiestRoutes(stats.busiestRoutes || []);
        }
    } catch (error) {
        console.error('Error loading dashboard:', error);
        loadMockDashboardData();
    }
}

function loadMockDashboardData() {
    const mockStats = {
        totalAirlines: 12,
        totalFlights: 345,
        totalTerminals: 28,
        totalPassengers: 15670,
        totalAircraft: 45,
        flightStatusDistribution: {
            'Scheduled': 45,
            'Departed': 30,
            'Delayed': 15,
            'Cancelled': 10
        },
        revenueByAirline: [
            { airline: 'Air India', revenue: 500000 },
            { airline: 'IndiGo', revenue: 750000 },
            { airline: 'SpiceJet', revenue: 600000 },
            { airline: 'Vistara', revenue: 400000 }
        ],
        busiestRoutes: [
            { route: 'Delhi -> Mumbai', flights: 25 },
            { route: 'Mumbai -> Bangalore', flights: 20 },
            { route: 'Delhi -> Bangalore', flights: 18 },
            { route: 'Mumbai -> Chennai', flights: 15 }
        ]
    };

    updateDashboardStats(mockStats);
    updateCharts(mockStats);
    updateBusiestRoutes(mockStats.busiestRoutes);
}

function updateDashboardStats(stats) {
    document.getElementById('total-airlines').textContent = stats.totalAirlines || 0;
    document.getElementById('total-flights').textContent = stats.totalFlights || 0;
    document.getElementById('total-terminals').textContent = stats.totalTerminals || 0;
    document.getElementById('total-passengers').textContent = stats.totalPassengers || 0;
}

function updateCharts(stats) {
    updateStatusChart(stats.flightStatusDistribution || {});
    updateRevenueChart(stats.revenueByAirline || []);
}

function updateStatusChart(statusData) {
    const ctx = document.getElementById('statusChart').getContext('2d');

    if (statusChart) {
        statusChart.destroy();
    }

    const labels = Object.keys(statusData);
    const data = Object.values(statusData);
    const colors = ['#4CAF50', '#2196F3', '#FF9800', '#F44336'];

    statusChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: colors,
                borderWidth: 2,
                borderColor: '#fff'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

function updateRevenueChart(revenueData) {
    const ctx = document.getElementById('revenueChart').getContext('2d');

    if (revenueChart) {
        revenueChart.destroy();
    }

    const labels = revenueData.map(item => item.airline);
    const data = revenueData.map(item => item.revenue);

    revenueChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Revenue (₹)',
                data: data,
                backgroundColor: 'rgba(102, 126, 234, 0.8)',
                borderColor: 'rgba(102, 126, 234, 1)',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return '₹' + value.toLocaleString();
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
}

function updateBusiestRoutes(routesData) {
    const tbody = document.getElementById('routes-tbody');
    tbody.innerHTML = '';

    routesData.forEach(route => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${route.route}</td>
            <td>${route.flights}</td>
        `;
    });
}

// Airlines functions
async function loadAirlines() {
    try {
        const airlines = await apiCall('/airlines');
        if (airlines) {
            currentData.airlines = airlines;
            updateAirlinesTable(airlines);
        }
    } catch (error) {
        console.error('Error loading airlines:', error);
    }
}

function updateAirlinesTable(airlines) {
    const tbody = document.getElementById('airlines-tbody');
    tbody.innerHTML = '';

    if (!airlines || airlines.length === 0) {
        const row = tbody.insertRow();
        row.innerHTML = '<td colspan="3" style="text-align:center">No airlines found</td>';
        return;
    }

    airlines.forEach(airline => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${airline.airlineId}</td>
            <td>${airline.name}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deleteAirline(${airline.airlineId})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </td>
        `;
    });
}

async function handleAirlineSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const airlineData = {
        airlineId: parseInt(formData.get('airlineId')),
        name: formData.get('name')
    };

    const result = await apiCall('/airlines', 'POST', airlineData);
    if (result) {
        closeModal('airline-modal');
        loadAirlines();
        showNotification('Airline added successfully!', 'success');
    }
}

async function deleteAirline(airlineId) {
    if (confirm('Are you sure you want to delete this airline?')) {
        const result = await apiCall(`/airlines/${airlineId}`, 'DELETE');
        if (result !== null) {
            loadAirlines();
            showNotification('Airline deleted successfully!', 'success');
        }
    }
}

// Terminals functions
async function loadTerminals() {
    try {
        const terminals = await apiCall('/terminals');
        if (terminals) {
            currentData.terminals = terminals;
            updateTerminalsTable(terminals);
        }
    } catch (error) {
        console.error('Error loading terminals:', error);
    }
}

function updateTerminalsTable(terminals) {
    const tbody = document.getElementById('terminals-tbody');
    tbody.innerHTML = '';

    if (!terminals || terminals.length === 0) {
        const row = tbody.insertRow();
        row.innerHTML = '<td colspan="5" style="text-align:center">No terminals found</td>';
        return;
    }

    terminals.forEach(terminal => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${terminal.terminalId}</td>
            <td>${terminal.name}</td>
            <td>${terminal.city || 'N/A'}</td>
            <td>${terminal.country || 'N/A'}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deleteTerminal(${terminal.terminalId})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </td>
        `;
    });
}

async function handleTerminalSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const terminalData = {
        terminalId: parseInt(formData.get('terminalId')),
        name: formData.get('name'),
        city: formData.get('city'),
        country: formData.get('country')
    };

    const result = await apiCall('/terminals', 'POST', terminalData);
    if (result) {
        closeModal('terminal-modal');
        loadTerminals();
        showNotification('Terminal added successfully!', 'success');
    }
}

async function deleteTerminal(terminalId) {
    if (confirm('Are you sure you want to delete this terminal?')) {
        const result = await apiCall(`/terminals/${terminalId}`, 'DELETE');
        if (result !== null) {
            loadTerminals();
            showNotification('Terminal deleted successfully!', 'success');
        }
    }
}

// Routes functions
async function loadRoutes() {
    console.log('Loading routes...');
    try {
        const routes = await apiCall('/routes');
        console.log('Routes data received:', routes);
        console.log('Number of routes:', routes ? routes.length : 0);

        if (routes) {
            currentData.routes = routes;
            updateRoutesTable(routes);
        } else {
            console.warn('No routes data received');
            updateRoutesTable([]);
        }
    } catch (error) {
        console.error('Error loading routes:', error);
        updateRoutesTable([]);
    }
}

function updateRoutesTable(routes) {
    console.log('Updating routes table with:', routes);
    const tbody = document.getElementById('routes-tbody-management');

    if (!tbody) {
        console.error('Routes table body not found!');
        return;
    }

    tbody.innerHTML = '';

    if (!routes || routes.length === 0) {
        const row = tbody.insertRow();
        row.innerHTML = '<td colspan="5" style="text-align:center">No routes found</td>';
        return;
    }

    routes.forEach((route, index) => {
        try {
            console.log(`Processing route ${index}:`, route);

            const row = tbody.insertRow();
            const airlineName = route.airline?.name || 'Unknown Airline';
            const originName = route.originTerminal?.name || 'Unknown Origin';
            const destName = route.destinationTerminal?.name || 'Unknown Destination';

            row.innerHTML = `
                <td>${route.routeId || 'N/A'}</td>
                <td>${airlineName}</td>
                <td>${originName}</td>
                <td>${destName}</td>
                <td>
                    <button class="btn btn-sm btn-danger" onclick="deleteRoute(${route.routeId})">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </td>
            `;
        } catch (error) {
            console.error('Error rendering route:', route, error);
        }
    });

    console.log('Routes table updated successfully');
}

async function handleRouteSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const routeData = {
        airline: { airlineId: parseInt(formData.get('airlineId')) },
        originTerminal: { terminalId: parseInt(formData.get('originTerminalId')) },
        destinationTerminal: { terminalId: parseInt(formData.get('destinationTerminalId')) }
    };

    const result = await apiCall('/routes', 'POST', routeData);
    if (result) {
        closeModal('route-modal');
        loadRoutes();
        showNotification('Route added successfully!', 'success');
    }
}

async function deleteRoute(routeId) {
    if (confirm('Are you sure you want to delete this route?')) {
        const result = await apiCall(`/routes/${routeId}`, 'DELETE');
        if (result !== null) {
            loadRoutes();
            showNotification('Route deleted successfully!', 'success');
        }
    }
}

// Flights functions
async function loadFlights() {
    try {
        const flights = await apiCall('/flights');
        if (flights) {
            currentData.flights = flights;
            updateFlightsTable(flights);
        }
    } catch (error) {
        console.error('Error loading flights:', error);
    }
}

function updateFlightsTable(flights) {
    const tbody = document.getElementById('flights-tbody');
    tbody.innerHTML = '';

    if (!flights || flights.length === 0) {
        const row = tbody.insertRow();
        row.innerHTML = '<td colspan="6" style="text-align:center">No flights found</td>';
        return;
    }

    flights.forEach(flight => {
        const row = tbody.insertRow();
        const routeText = flight.route ?
            `${flight.route.originTerminal?.name || 'Unknown'} → ${flight.route.destinationTerminal?.name || 'Unknown'}` :
            'Unknown Route';
        const aircraftText = flight.aircraft?.model || 'Unknown Aircraft';
        const departureTime = new Date(flight.departureTime).toLocaleString();

        row.innerHTML = `
            <td>${flight.flightId}</td>
            <td>${routeText}</td>
            <td>${aircraftText}</td>
            <td>${departureTime}</td>
            <td><span class="status-badge status-${flight.status.toLowerCase()}">${flight.status}</span></td>
            <td>
                <button class="btn btn-sm btn-success" onclick="updateFlightStatus(${flight.flightId}, 'Departed')">
                    <i class="fas fa-plane-departure"></i> Depart
                </button>
                <button class="btn btn-sm btn-danger" onclick="updateFlightStatus(${flight.flightId}, 'Cancelled')">
                    <i class="fas fa-times"></i> Cancel
                </button>
            </td>
        `;
    });
}

async function updateFlightStatus(flightId, status) {
    const result = await apiCall(`/flights/${flightId}/status?status=${status}`, 'PUT');
    if (result) {
        loadFlights();
        showNotification(`Flight status updated to ${status}!`, 'success');
    }
}

// Passengers functions
async function loadPassengers() {
    try {
        const passengers = await apiCall('/passengers');
        if (passengers) {
            currentData.passengers = passengers;
            updatePassengersTable(passengers);
        }
    } catch (error) {
        console.error('Error loading passengers:', error);
    }
}

function updatePassengersTable(passengers) {
    const tbody = document.getElementById('passengers-tbody');
    tbody.innerHTML = '';

    if (!passengers || passengers.length === 0) {
        const row = tbody.insertRow();
        row.innerHTML = '<td colspan="5" style="text-align:center">No passengers found</td>';
        return;
    }

    passengers.forEach(passenger => {
        const row = tbody.insertRow();
        const dob = passenger.dateOfBirth ? new Date(passenger.dateOfBirth).toLocaleDateString() : 'N/A';

        row.innerHTML = `
            <td>${passenger.passengerId}</td>
            <td>${passenger.firstName}</td>
            <td>${passenger.lastName}</td>
            <td>${dob}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deletePassenger(${passenger.passengerId})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </td>
        `;
    });
}

async function handlePassengerSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const passengerData = {
        firstName: formData.get('firstName'),
        lastName: formData.get('lastName'),
        dateOfBirth: formData.get('dateOfBirth') || null
    };

    const result = await apiCall('/passengers', 'POST', passengerData);
    if (result) {
        closeModal('passenger-modal');
        loadPassengers();
        showNotification('Passenger added successfully!', 'success');
    }
}

async function deletePassenger(passengerId) {
    if (confirm('Are you sure you want to delete this passenger?')) {
        const result = await apiCall(`/passengers/${passengerId}`, 'DELETE');
        if (result !== null) {
            loadPassengers();
            showNotification('Passenger deleted successfully!', 'success');
        }
    }
}

// Aircraft functions
async function loadAircraft() {
    try {
        const aircraft = await apiCall('/aircraft');
        if (aircraft) {
            currentData.aircraft = aircraft;
            updateAircraftTable(aircraft);
        }
    } catch (error) {
        console.error('Error loading aircraft:', error);
    }
}

function updateAircraftTable(aircraft) {
    const tbody = document.getElementById('aircraft-tbody');
    tbody.innerHTML = '';

    if (!aircraft || aircraft.length === 0) {
        const row = tbody.insertRow();
        row.innerHTML = '<td colspan="4" style="text-align:center">No aircraft found</td>';
        return;
    }

    aircraft.forEach(craft => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${craft.aircraftId}</td>
            <td>${craft.model}</td>
            <td>${craft.capacity}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deleteAircraft(${craft.aircraftId})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </td>
        `;
    });
}

async function handleAircraftSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const aircraftData = {
        model: formData.get('model'),
        capacity: parseInt(formData.get('capacity'))
    };

    const result = await apiCall('/aircraft', 'POST', aircraftData);
    if (result) {
        closeModal('aircraft-modal');
        loadAircraft();
        showNotification('Aircraft added successfully!', 'success');
    }
}

async function deleteAircraft(aircraftId) {
    if (confirm('Are you sure you want to delete this aircraft?')) {
        const result = await apiCall(`/aircraft/${aircraftId}`, 'DELETE');
        if (result !== null) {
            loadAircraft();
            showNotification('Aircraft deleted successfully!', 'success');
        }
    }
}

// Tickets functions
async function loadTickets() {
    console.log('Loading tickets...');
    try {
        const tickets = await apiCall('/tickets');
        console.log('Tickets data received:', tickets);
        console.log('Number of tickets:', tickets ? tickets.length : 0);

        if (tickets) {
            currentData.tickets = tickets;
            updateTicketsTable(tickets);
        } else {
            console.warn('No tickets data received');
            updateTicketsTable([]);
        }
    } catch (error) {
        console.error('Error loading tickets:', error);
        updateTicketsTable([]);
    }
}

function updateTicketsTable(tickets) {
    console.log('Updating tickets table with:', tickets);
    const tbody = document.getElementById('tickets-tbody');

    if (!tbody) {
        console.error('Tickets table body not found!');
        return;
    }

    tbody.innerHTML = '';

    if (!tickets || tickets.length === 0) {
        const row = tbody.insertRow();
        row.innerHTML = '<td colspan="6" style="text-align:center">No tickets found</td>';
        return;
    }

    tickets.forEach((ticket, index) => {
        try {
            console.log(`Processing ticket ${index}:`, ticket);

            const row = tbody.insertRow();
            const passengerName = ticket.passenger ?
                `${ticket.passenger.firstName || ''} ${ticket.passenger.lastName || ''}`.trim() :
                'Unknown';
            const flightId = ticket.flight?.flightId || 'N/A';
            const price = ticket.price ? `₹${ticket.price.toFixed(2)}` : 'N/A';

            row.innerHTML = `
                <td>${ticket.ticketId || 'N/A'}</td>
                <td>${flightId}</td>
                <td>${passengerName}</td>
                <td>${ticket.seatNumber || 'N/A'}</td>
                <td>${price}</td>
                <td>
                    <button class="btn btn-sm btn-danger" onclick="deleteTicket(${ticket.ticketId})">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </td>
            `;
        } catch (error) {
            console.error('Error rendering ticket:', ticket, error);
        }
    });

    console.log('Tickets table updated successfully');
}

async function handleTicketSubmit(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const ticketData = {
        flight: { flightId: parseInt(formData.get('flightId')) },
        passenger: { passengerId: parseInt(formData.get('passengerId')) },
        seatNumber: formData.get('seatNumber'),
        price: parseFloat(formData.get('price'))
    };

    const result = await apiCall('/tickets', 'POST', ticketData);
    if (result) {
        closeModal('ticket-modal');
        loadTickets();
        showNotification('Ticket added successfully!', 'success');
    }
}

async function deleteTicket(ticketId) {
    if (confirm('Are you sure you want to delete this ticket?')) {
        const result = await apiCall(`/tickets/${ticketId}`, 'DELETE');
        if (result !== null) {
            loadTickets();
            showNotification('Ticket deleted successfully!', 'success');
        }
    }
}

// Notification system
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <span>${message}</span>
        <button onclick="this.parentElement.remove()">&times;</button>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}
