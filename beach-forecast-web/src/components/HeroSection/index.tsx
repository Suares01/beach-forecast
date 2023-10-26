import Image from 'next/image';
import Link from 'next/link';

export default function HeroSection() {
  return (
    <section>
      <div className="mx-auto max-w-screen-2xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 lg:h-screen lg:grid-cols-2">
          <div className="relative z-10 lg:py-16">
            <div className="relative h-64 sm:h-80 lg:h-full">
              <Image
                src="/praia-home.avif"
                alt="Beach"
                className="absolute  inset-0 object-cover"
                fill
                priority
              />
            </div>
          </div>

          <div className="relative flex items-center bg-gray-100">
            <span className="hidden lg:absolute lg:inset-y-0 lg:-start-16 lg:block lg:w-16 lg:bg-gray-100"></span>

            <div className="p-8 sm:p-16 lg:p-24">
              <h2 className="text-2xl font-bold sm:text-3xl">
                Always know the <br /> best beach to go to
              </h2>

              <p className="mt-4 text-gray-600">
                With Beach Forecast you register your favorite beaches, receive
                weather and marine forecasts and receive an evaluation for each
                forecast!
              </p>

              <Link
                href="/signup"
                className="mt-8 inline-block rounded border border-sky-600 bg-sky-600 px-12 py-3 text-sm font-medium text-white hover:bg-transparent hover:text-indigo-600 focus:outline-none focus:ring active:text-indigo-500"
              >
                Try now
              </Link>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
