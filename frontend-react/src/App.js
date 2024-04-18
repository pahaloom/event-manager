import * as react from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';

const Home = react.lazy(() => import("./routes/Home"));
const Event = react.lazy(() => import("./routes/Event"));

function App() {
  return (
    <Router>
      <div className="App">
        <ul>
          <li><Link to="/">Home</Link></li>
          <li><Link to="/event">Event</Link></li>
        </ul>

        <react.Suspense fallback={<>Loading...</>}>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/event" element={<Event />} />
          </Routes>
        </react.Suspense>
      </div>
    </Router>
  );
}

export default App;
