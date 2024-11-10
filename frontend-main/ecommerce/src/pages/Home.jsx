import React from "react";
import Header from "../components/header/Header";
import Navbar from "../components/header/Navbar";
import Slider from "../components/slider/Slider";
import Assurance from "../components/whychoose/Assurance";
import Services from "../components/welcome/Services";
import Welcome from "../components/welcome/Welcome";
import InfoCard from "../components/whychoose/InfoCard";
import Footer from "../components/footer/Footer";
import WorkingProcess from "../components/workingprocess/WorkingProcess";

import JoinBoth from "../components/faq/JoinBoth";
import StatisticCard from "../components/staticsdata/StatisticCard";

function Home() {
  return (
    <div>
      <Header />
      <Navbar />
      <Slider />
      <Welcome />
      <Services />
      <StatisticCard />
      <Assurance />
      <WorkingProcess />
      <JoinBoth />
      <InfoCard />
      <Footer />
      {/* <Footer /> */}
    </div>
  );
}

export default Home;
