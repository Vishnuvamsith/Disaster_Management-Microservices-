import { c1,c2,c3,c4,c5,c6,c7,c8 } from "../../assets/home";
import Container from "../Container";
import SectionTitle from "./SectionTitle";

const cards = [
  {
    id: 1,
    image: c1,
    title: "Disaster Management Authorities",
  },
  {
    id: 2,
    image: c2,
    title: "Simulations",
  },
  {
    id: 3,
    image: c3,
    title: "Resource Library",
  },
  {
    id: 4,
    image: c4,
    title: "Awareness & Schemes",
  },
  {
    id: 1,
    image: c5,
    title: "Volunteer",
  },
  {
    id: 2,
    image: c6,
    title: "Fundraiser",
  },
  {
    id: 3,
    image: c7,
    title: "Community",
  },
  {
    id: 4,
    image: c8,
    title: "Do's & Dont's",
  },

];

const DiscoverCard = ({ card }) => {
  return (
    <div className="relative rounded-xl overflow-hidden cursor-pointer hover:scale-105 transition-transform">
      <img src={card.image} alt="discover_image" />
      <div className="absolute bottom-6 capitalize left-5 text-white font-bold sm:text-[40px]">
        {card.title}
      </div>
    </div>
  );
};

export default function Discover() {
  return (
    <section className="my-14">
      <Container>
        <div>
          <SectionTitle title="services" />
          <div className="grid sm:grid-cols-3 mt-4 sm:grid-cols-4 grid-cols-1 place-items-center lg:gap-10 gap-4">
            {cards.map((card) => (
              <DiscoverCard card={card} key={card.id} />
            ))}
          </div>
        </div>
      </Container>
    </section>
  );
}
